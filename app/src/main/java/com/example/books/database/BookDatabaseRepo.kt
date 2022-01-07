package com.example.books.database

import android.content.Context
import android.media.Rating
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.okhttp.internal.DiskLruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

import kotlin.Exception

private const val TAG = "BookDatabaseRepo"
class BookDatabaseRepo private constructor(context: Context){

private val auth: FirebaseAuth = FirebaseAuth.getInstance()
private val firestore:FirebaseFirestore = FirebaseFirestore.getInstance()
private val storge = FirebaseStorage.getInstance()
private val storageRef = storge.reference
    private val booksCollectionRef = Firebase.firestore.collection("books")
    private val bookList = mutableListOf<Book>()
    lateinit var bookId: String

    fun insertBook(book: Book){


           val Id =  booksCollectionRef.document()
                    book.bookId = Id.id
                      Id.set(book)


        }

    fun deleteBook(book: Book){
        Log.d(TAG, "deleteBook: ${book.bookId}")
   val db = Firebase.firestore.collection("books")

        db.document(book.bookId).delete()


            }



   suspend fun getAllBook(): LiveData<List<Book>> {

       return liveData {
           val books = mutableListOf<Book>()
          booksCollectionRef.get().await().forEach {
              val book =Book()
              book.bookName = it.getString("bookName")!!
              Log.d(TAG, "getAllBook: ${book.bookName}")
              book.authorName= it.getString("authorName")!!
              book.bookImage= it.getString("bookImage")!!
              book.pdfFile=it.getString("pdfFile")!!
              book.bookOwner= it.getString("bookOwner")!!
              book.yearOfPublication=it.getString("yearOfPublication")!!
//              comment.commentText=it.getString("comment")!!
              book.bookId = it.id
//              bookId = it.id
              books+=book
          }
           emit(books)
       }
    }


    fun addComment(comment: Comment , bookId: String){

       booksCollectionRef.document(bookId ).update("comment",FieldValue.arrayUnion(comment))

}

    suspend fun getAllComment(bookId:String):List<Comment>{
     val document =   booksCollectionRef.document(bookId).get().await()

        val comment : MutableList<Comment> = document["comment"] as MutableList<Comment>


        Log.d(TAG,"$comment")

        return comment

    }


    fun updateRating(bookId: String , userId:String) {

        booksCollectionRef.whereEqualTo("rating" ,userId )
            .get().addOnSuccessListener {
                it.apply {


                }

            }

    }


   fun rating(){

       val ref = FirebaseDatabase.getInstance().getReference("books")
       var numbers: ArrayList<Int> = arrayListOf() // Change to whatever type is accurate in your case
       var sum = 0

       ref.addListenerForSingleValueEvent(object : ValueEventListener {
           override fun onDataChange(p0: DataSnapshot) {
               p0.children.forEach {
                   val rt = it.child("rating").value
                   numbers.add(rt as Int)

               }

               // After the forEach loop is finished you should have all the ratings in the numbers array

           }

           override fun onCancelled(error: DatabaseError) {
               Log.d(TAG, "onCancelled: ")
           }
       })

   }

    fun updateBookInformation(){



    }
  suspend fun getBook(bookId: String): Book? {

        val bookRef = Firebase.firestore.collection("books").document(bookId)
        return bookRef.get().await().toObject(Book::class.java)
//      val bookRef = Firebase.firestore.collection("books").document(bookId)
//      return bookRef.get().addOnSuccessListener {
//          if (it != null){
//            val book = it.toObject(Book::class.java)
//              fi
//          }
//
//
//      }
    }

    fun addBookRating(bookId: String, ratingBook: RatingBook){

      booksCollectionRef.document(bookId ).update("rating",FieldValue.arrayUnion(ratingBook))


    }

    fun getBookRating(bookId:String):List<Float>{
        var rating:List<Float> = emptyList()
       booksCollectionRef.document(bookId).get().addOnSuccessListener {
          rating = it["rating"] as List<Float>
           Log.e(TAG, "$rating")
      }
        return rating
    }

    suspend fun getFav(bookId: List<Favorite>):LiveData<List<Book>>{
        val books = mutableListOf<Book>()
       return liveData {

           bookId.forEach { fav ->

               booksCollectionRef.document(fav.bookId).get().await().toObject(Book::class.java)?.let {
                   books.add(
                       it
                   )
               }
           }

           emit(books)

       }




    }




companion object {

    private var INSTANT: BookDatabaseRepo? = null

    fun initiliza(context: Context) {

        if (INSTANT == null) {
            INSTANT = BookDatabaseRepo(context)

        }

    }

    fun getInstant(): BookDatabaseRepo = INSTANT ?: throw IllegalStateException(" repo has not be init")

}}








