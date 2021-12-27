package com.example.books.fragments.bookdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.commentFragment.Comment
import com.example.books.databinding.CommentListItemBinding
import com.example.books.databinding.FragmentBookDetailsBinding
import com.example.books.fragments.homepagefragment.HomePageFragment
import com.example.books.fragments.homepagefragment.HomePageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private const val TAG = "BookDetailsFragment"
class BookDetailsFragment : Fragment() {


    private val bookDetailsViewModel by lazy { ViewModelProvider(this)[BookDetailsViewModel::class.java] }

private lateinit var binding: FragmentBookDetailsBinding
private val firebase = FirebaseAuth.getInstance()
    private lateinit var book: Book
    private lateinit var comment: Comment
    private val commentList = mutableListOf<Comment>()
    private val db = FirebaseFirestore.getInstance()
    val firestore= Firebase.firestore.collection("books")

    private lateinit var auth: FirebaseAuth
//    val firestore = Firebase.firestore.collection("books").whereEqualTo("bookId" ,book.bookId)
    private val args: BookDetailsFragmentArgs by navArgs()
    lateinit var bookId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = Book()
//        comment=Comment()
        auth = FirebaseAuth.getInstance()
         bookId = args.bookId as String
        Log.d(TAG, "onCreate: $bookId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentBookDetailsBinding.inflate(layoutInflater)
//        getBookData()
 bookDetailsViewModel.getBook(bookId)
        binding.sendCommentBtn.setOnClickListener {
            val commentText =binding.commentTv.text.toString()
            val comment = Comment(commentText)
//            booksCollectionRef.document().update("books", FieldValue.arrayUnion(comment))
//            bookId = args.bookId
            bookDetailsViewModel.addComment(comment,bookId)

        }
        binding.commentRv.layoutManager=LinearLayoutManager(context)
        binding.commentRv.adapter=CommentAdapter(book.comment)

        return binding.root
    }


private inner class CommentHolder(val binding: CommentListItemBinding):RecyclerView.ViewHolder(binding.root){
    private lateinit var comment: Comment

    fun bind(comment: Comment){

        this.comment = comment
        binding.commentTv.text=comment.commentText


    }


}

    private inner class CommentAdapter(val comments :List<Comment>):RecyclerView.Adapter<CommentHolder> (){
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
        }


    }


    private fun getBookData(){
        val firestore = Firebase.firestore.collection("books").whereEqualTo("bookId" ,book.bookId)
        firestore.get().addOnSuccessListener {

            if (it != null){
                binding.imageBookTv.load(book.bookImage)
                
                binding.BookNameTv.setText(it.toString())
                binding.authorNameTv.setText(it.toString())
                binding.yearOfBookTv.setText(it.toString())



            }
        }



    }

}