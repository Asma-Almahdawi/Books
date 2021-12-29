package com.example.books.fragments.bookdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.database.RatingBook
import com.example.books.databinding.CommentListItemBinding
import com.example.books.databinding.FragmentBookDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


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
    var ratingAverage =0f

    private lateinit var auth: FirebaseAuth
//    val firestore = Firebase.firestore.collection("books").whereEqualTo("bookId" ,book.bookId)
    private val args: BookDetailsFragmentArgs by navArgs()
    lateinit var bookId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        comment=Comment()
        auth = FirebaseAuth.getInstance()
         bookId = args.bookId as String
        Log.d(TAG, "onCreate: $bookId")

//        bookDetailsViewModel.getComments(bookId).observe(
//              this, Observer {
//                Log.d(TAG, "onCreate: $it")
//                  binding.commentRv.adapter=CommentAdapter(it)
//            }
//        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentBookDetailsBinding.inflate(layoutInflater)
//        getBookData()

        lifecycleScope.launch(){
            book = bookDetailsViewModel.getBook(bookId) ?: Book()
            binding.BookNameTv.setText(book.bookName)
            Log.d(TAG, "Book: $book")
        }.invokeOnCompletion {
            binding.commentRv.adapter=CommentAdapter(book.comment)
            Log.d(TAG, "onCreateView: ${book.comment}")
            book.rating.forEach {

                ratingAverage += it.userRating.toFloat()



            }
            ratingAverage =ratingAverage / book.rating.size
            binding.ratingBar.rating = ratingAverage

        }
        binding.sendCommentBtn.setOnClickListener {
            val commentText =binding.commentTv.text.toString()
            val comment = Comment( useraId = auth.currentUser!!.uid, commentText)
//            booksCollectionRef.document().update("books", FieldValue.arrayUnion(comment))
//            bookId = args.bookId
            bookDetailsViewModel.addComment(comment,bookId)

        }
        binding.commentRv.layoutManager=LinearLayoutManager(context)
//
//          binding.ratingBar.rating = 1f
        book = Book()
//        bookDetailsViewModel.getRating(bookId,book.rating)

        binding.ratingBar.setOnClickListener {



        }

//        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
//
//            var total = 0.0
//            var count = 0.0
//            var average = 0.0
//            total += rating;
//            count += 1
//            average=  total / count
//            bookDetailsViewModel.getRating(bookId ,rating)
//        }

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->

            val ratingBook = RatingBook(userRating =rating.toString(), userId = auth.currentUser!!.uid )
            bookDetailsViewModel.addBookRating(bookId,ratingBook)

        }

        binding.sendRate.setOnClickListener {



////             binding.ratingBar.rating
//
//                 var rating = 0f
//
//            book.rating.forEach {
//
//                val count = 1f
//                count+1
//
//              val average =  count/ binding.ratingBar.rating
//
//            }

//            val rating =  mutableMapOf(auth.currentUser!!.uid to binding.ratingBar.rating)





//          val bookRating: MutableList<Float> = bookDetailsViewModel.getBookRating(bookId).toMutableList()
//            when(bookRating){
//                null -> binding.ratingBar.rating = 5F
//                else -> binding.ratingBar.rating = bookRating.sum().toFloat()/5
//            }

//            val userRating = binding.ratingBar.rating
//            val ind = bookRating.size
//            bookRating.add(userRating)
//            var rating:MutableList<Float> = bookRating
//            bookDetailsViewModel.getRating(bookId ,rating)

            
//
//book.rating.forEach {
//    var count =0.1
//        count += 1
//
//   val  average = count / binding.ratingBar.stepSize
//
//
//}

        }
//        binding.ratingBar.rating = 5f
//        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
//
//            bookDetailsViewModel.rating()
//
//        }



        return binding.root
    }




//    fun getRating(ratings: List<Rating> , book: Book):Float{
//
//        var count = 1f
//        ratings.forEach{
//
//            count += it
//            book.value= (count / ratings.size).toString()
//        }
//        return
//
//    }
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
            Log.d(TAG, "getItemCount:${comments.size} ")
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