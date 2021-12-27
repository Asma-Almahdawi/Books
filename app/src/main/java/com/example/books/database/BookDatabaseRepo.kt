package com.example.books.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.internal.DiskLruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

import kotlin.Exception

private const val TAG = "BookDatabaseRepo"
class BookDatabaseRepo {
    private val database = FirebaseFirestore.getInstance()
    private val booksCollectionRef = Firebase.firestore.collection("books")
    private val bookList = mutableListOf<Book>()
    lateinit var bookId: String

    fun insertBook(book: Book){

//        try {
//         booksCollectionRef.add(book)

           val Id =  booksCollectionRef.document()
                    book.bookId = Id.id
                      Id.set(book)

//                .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
//            }.addOnFailureListener { e ->
//                    Log.w(TAG, "Error adding document", e)
//                }
//
//        } catch (e: Exception) {
//                Log.d(TAG, " cannot SAVE DATA ")
//
//            }
        }

    fun deleteBook(book: Book){
        Log.d(TAG, "deleteBook: ${book.bookId}")
   val db = Firebase.firestore.collection("books")

        db.document(book.bookId).delete()
//        booksCollectionRef.get()
//            .addOnSuccessListener {
//
//            it.forEach {
//
//                if (it.id == book.bookId) {
//                    Log.d("DELETEE", "YES")
//                    booksCollectionRef.document(book.bookId).delete()
//                    Log.d("DELETEE", "YESssssssss")
//
//                }
//            }
//            }
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
              book.bookId = it.id
//              bookId = it.id
              books+=book
          }
           emit(books)
       }
    }
//        db.collection("books").get().addOnSuccessListener {
//            for (doc in it){
//                val book = doc.toObject(Book::class.java)
//                bookList.add(book)
//                Log.d(com.example.books.fragments.homepagefragment.TAG," GET DATA $bookList")
//
//            }

    fun addComment(comment: Comment , bookId: String){

       booksCollectionRef.document(bookId ).update("comment",FieldValue.arrayUnion(comment))


//        booksCollectionRef.document(bookId).update("books",FieldValue.arrayUnion(comment))


//        return liveData {
//
//            val comments = mutableListOf<Comment>()
//            booksCollectionRef.get().await().forEach {
//                val comment =Comment()
////                comment.commentText = it.getString("commentText")!!
//                comment.bookIdOwner= it.getString("bookOwner")!!
//                comment.bookId = it.getString("bookId")!!
////              bookId = it.id
//                comments+=comment
//            }
//            emit(comments)
//            booksCollectionRef.document(bookId).update("books",FieldValue.arrayUnion(comment))
//        }


//        booksCollectionRef.add(comment)
//        val Id =  booksCollectionRef.document()
//        book.bookId = Id.id
//        booksCollectionRef.document(Id.id).set(comment)


//        booksCollectionRef.document().update("books", FieldValue.arrayUnion(comment))
//        val commentId = booksCollectionRef.whereEqualTo("comment" , book.Id)
//        val commentId =  booksCollectionRef.document()
//        book.bookId = commentId.id
//        commentId.set(comment)


}
    suspend fun getAllComment(comment: Comment):LiveData<List<Comment>>{

//        booksCollectionRef.document(bookId).update("books",FieldValue.arrayUnion(comment))

//
//        return liveData {
//
//            val comments = mutableListOf<Comment>()
//                .forEach {
//                val comment =Comment()
//                comment.commentText = it.getString("commentText")!!
//                comment.bookId = it.id
////              bookId = it.id
//                comments+=comment
//            }
//            emit(comments))
//        }
//
//
//        }
        return liveData {




        }
    }

    fun updateBookInformation(){



    }
    fun getBook(bookId: String) {
        // [START get_document]
        val bookRef = Firebase.firestore.collection("books").document(bookId)
        bookRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        // [END get_document]
    }


}





