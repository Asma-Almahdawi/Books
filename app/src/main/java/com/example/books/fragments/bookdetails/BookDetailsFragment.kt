package com.example.books.fragments.bookdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.UserComment
import com.example.books.database.Favorite
import com.example.books.database.RatingBook
import com.example.books.database.User
import com.example.books.databinding.CommentListItemBinding
import com.example.books.databinding.FragmentBookDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.*


private const val TAG = "BookDetailsFragment"
class BookDetailsFragment : Fragment() {


    private val bookDetailsViewModel by lazy { ViewModelProvider(this)[BookDetailsViewModel::class.java] }
private lateinit var binding: FragmentBookDetailsBinding

    private lateinit var book: Book
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth

    var ratingAverage =0f


    private val args: BookDetailsFragmentArgs by navArgs()
    lateinit var bookId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()

user=User()
         bookId = args.bookId as String
        Log.d(TAG, "onCreate: $bookId")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentBookDetailsBinding.inflate(layoutInflater)
//        getBookData()

        lifecycleScope.launch(){
             book = bookDetailsViewModel.getBook(bookId) ?: Book()
            Log.d(TAG, "onCreateView: ")
            binding.BookNameTv.setText(book.bookName)
            Log.d(TAG, "onCreateView: ${book.bookName}")
            binding.authorNameTv.setText(book.bookOwner)
            binding.authorNameTv.setText(book.authorName)
            binding.imageBookTv.load(book.bookImage)
            binding.yearOfBookTv.setText(book.yearOfPublication)



            Log.d(TAG, "Book: $book")
        }.invokeOnCompletion {

            lifecycleScope.launch {
                 bookDetailsViewModel.getComment(bookId).observe(
                     viewLifecycleOwner ,{
                         binding.commentRv.adapter=CommentAdapter(it)
                         Log.d(TAG, "onCreateView: ${it.forEach {
                             Log.d(TAG, "onCreateView: $it")
                         }}")
                     }
                 )

            }

            Log.d(TAG, "onCreateView: ${book.comment}")


            book.rating.forEach {

                ratingAverage += it.userRating.toFloat()



            }
            ratingAverage /= book.rating.size
//            binding.ratingBar.rating = ratingAverage
            binding.rateTv.text= ratingAverage.toString()

        }
        binding.sendCommentBtn.setOnClickListener {
            user= User()
            val commentText =binding.commentTv.text.toString()
            val comment = Comment( useraId = user.userId, commentText , commentId = UUID.randomUUID().toString())


            bookDetailsViewModel.addComment(comment,bookId)

        }
        binding.commentRv.layoutManager=LinearLayoutManager(context)


        book = Book()

        binding.ratingBar.setOnClickListener {



        }

        binding.favBtn.setOnClickListener {

            lifecycleScope.launch {
                val favorite=Favorite(bookId)
                bookDetailsViewModel.addToFavv(favorite , bookId)
            }

        }


        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->

            val ratingBook = RatingBook(userRating =rating.toString(), userId = auth.currentUser!!.uid)
            bookDetailsViewModel.addBookRating(bookId,ratingBook ,userId =auth.currentUser!!.uid)

        }


        binding.pdfView.setOnClickListener {
            val action =BookDetailsFragmentDirections.actionBookDetailsFragmentToPdfViewFragment(book.bookId)
            findNavController().navigate(action)
        }


    binding.BookNameTv.setOnClickListener {

        lifecycleScope.launch {

            bookDetailsViewModel.deleteRating(bookId, user.userId)

        }
    }

        return binding.root
    }





private inner class CommentHolder(val binding: CommentListItemBinding):RecyclerView.ViewHolder(binding.root){
    private var comment: Comment? = null




     fun bind(comment: UserComment){

        this.comment = comment.comment
        binding.commentTv.text= comment.comment?.commentText
         binding.imageUserTv.load(comment.user?.profileImageUrl)



    }




}

    private inner class CommentAdapter(val comments :List<UserComment>):RecyclerView.Adapter<CommentHolder> (){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
          val binding = CommentListItemBinding.inflate(
              layoutInflater,
              parent,
              false
          )

            return CommentHolder(binding)
        }

        override fun onBindViewHolder(holder: CommentHolder, position: Int) {

            val comment = comments[position]
            holder.bind(comment)


        }

        override fun getItemCount(): Int {

            return comments.size
            Log.d(TAG, "getItemCount:${comments.size} ")
        }


    }

    }

