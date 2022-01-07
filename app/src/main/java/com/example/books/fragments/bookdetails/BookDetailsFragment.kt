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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.Following
import com.example.books.database.Favorite
import com.example.books.database.RatingBook
import com.example.books.database.User
import com.example.books.databinding.CommentListItemBinding
import com.example.books.databinding.FragmentBookDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*


private const val TAG = "BookDetailsFragment"
class BookDetailsFragment : Fragment() {


    private val bookDetailsViewModel by lazy { ViewModelProvider(this)[BookDetailsViewModel::class.java] }
private lateinit var binding: FragmentBookDetailsBinding

    private lateinit var book: Book
    private lateinit var user: User

    var ratingAverage =0f


    private val args: BookDetailsFragmentArgs by navArgs()
    lateinit var bookId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        comment=Comment()
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

        lifecycleScope.launch {

            bookDetailsViewModel.getUserData().observe(
                viewLifecycleOwner,{
                    user = it


                }
            )

        }
        lifecycleScope.launch(){
             book = bookDetailsViewModel.getBook(bookId) ?: Book()
            Log.d(TAG, "onCreateView: ")
            binding.BookNameTv.setText(book.bookName)
            Log.d(TAG, "onCreateView: ${book.bookName}")
            binding.authorNameTv.setText(book.bookOwner)
            binding.authorNameTv.setText(book.authorName)
            binding.imageBookTv.load(book.bookImage)




            Log.d(TAG, "Book: $book")
        }.invokeOnCompletion {
            binding.commentRv.adapter=CommentAdapter(book.comment)
            Log.d(TAG, "onCreateView: ${book.comment}")
            book.rating.forEach {

                ratingAverage += it.userRating.toFloat()



            }
            ratingAverage /= book.rating.size
//            binding.ratingBar.rating = ratingAverage
            binding.rateTv.text= ratingAverage.toString()

        }
        binding.sendCommentBtn.setOnClickListener {
            val commentText =binding.commentTv.text.toString()
            val comment = Comment( useraId = user.userId, commentText , commentId = UUID.randomUUID().toString())
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

        binding.favBtn.setOnClickListener {

            lifecycleScope.launch {
                val favorite=Favorite(bookId)
                bookDetailsViewModel.addToFavv(favorite , bookId)
            }

        }

//    val following=Following(anotherUserId =user.userId)
//        binding.followBtn.setOnClickListener {
//
//
//
//                bookDetailsViewModel.following(following)
//
//        }

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

            val ratingBook = RatingBook(userRating =rating.toString(), userId = user.userId )
            bookDetailsViewModel.addBookRating(bookId,ratingBook)

        }

        binding.BookNameTv.setOnClickListener {
//            Log.d(TAG, "onCreateView: ${user.userId}")
//            val action = BookDetailsFragmentDirections.actionBookDetailsFragmentToProfileFragment(user.userId)
//            findNavController().navigate(action)

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

lifecycleScope.launch {
     bookDetailsViewModel.getUserData().observe(

         viewLifecycleOwner,Observer{
             binding.imageUserTv.load(it.profileImageUrl)

         }
//         binding.imageUserTv.load(user.profileImageUrl)


     )

}

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


//    private fun getBookData(){
//        val firestore = Firebase.firestore.collection("books").whereEqualTo("bookId" ,book.bookId)
//        firestore.get().addOnSuccessListener {
//
//            if (it != null){
//                binding.imageBookTv.load(book.bookImage)
//
//                binding.BookNameTv.setText(it.toString())
//                binding.authorNameTv.setText(it.toString())
//                binding.yearOfBookTv.setText(it.toString())
//
//
//
//            }
//        }


//                val storageRef = storage.reference
//
//                val pdfFileRef = storageRef.child("pdfs/file.pdf")
//
//                val localFile = File.createTempFile("files", "pdf")
//
//                pdfFileRef.getFile(localFile).addOnSuccessListener {
//                    // Local temp file has been created
//                    Log.d("TAG", "File Downloaded")
//                }.addOnFailureListener {
//                    // Handle any errors
//                    Log.d("TAG", "Something went wrong")
//                }


    }

