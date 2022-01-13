package com.example.books.database

import android.content.Context
import android.media.Rating
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.UserComment
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

class BookDatabaseRepo private constructor(context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storge = FirebaseStorage.getInstance()
    private val storageRef = storge.reference

    private val booksCollectionRef = Firebase.firestore.collection("books")
    private val userCollectionRef = firestore.collection("users")

    lateinit var bookId: String

    fun insertBook(book: Book) {
        booksCollectionRef.document(book.bookId).set(book)
    }

    suspend fun getUserBooks(): LiveData<List<Book>> {

        return liveData {

            val books = mutableListOf<Book>()
            booksCollectionRef.whereEqualTo("bookOwner", auth.currentUser!!.uid).get().await()
                .forEach {
                    val book = Book()
                    book.bookName = it.getString("bookName")!!
                    Log.d(TAG, "getAllBook: ${book.bookName}")
                    book.authorName = it.getString("authorName")!!
                    book.bookImage = it.getString("bookImage")!!
                    book.pdfFile = it.getString("pdfFile")!!
                    book.bookOwner = it.getString("bookOwner")!!
                    book.yearOfPublication = it.getString("yearOfPublication")!!
//              comment.commentText=it.getString("comment")!!
                    book.bookId = it.id
//              bookId = it.id
                    books += book

                }

            emit(books)

        }
    }


    fun deleteBook(book: Book) {

        booksCollectionRef.document(book.bookId).delete()


    }


    suspend fun getAllBook(): LiveData<List<Book>> {

        return liveData {
            val books = mutableListOf<Book>()
            booksCollectionRef.get().await().forEach {
                val book = Book()
                book.bookName = it.getString("bookName")!!
                Log.d(TAG, "getAllBook: ${book.bookName}")
                book.authorName = it.getString("authorName")!!
                book.bookImage = it.getString("bookImage")!!
                book.pdfFile = it.getString("pdfFile")!!
                book.bookOwner = it.getString("bookOwner")!!
                book.yearOfPublication = it.getString("yearOfPublication")!!
//              comment.commentText=it.getString("comment")!!
                book.bookId = it.id
//              bookId = it.id
                books += book
            }
            emit(books)
        }
    }


    fun addComment(comment: Comment, bookId: String) {
          comment.useraId= auth.currentUser!!.uid
        booksCollectionRef.document(bookId).update("comment", FieldValue.arrayUnion(comment))

    }


    suspend fun getBook(bookId: String): Book? {

        return booksCollectionRef.document(bookId).get().await().toObject(Book::class.java)


    }

    suspend fun getComment(bookId: String): LiveData<List<UserComment>> {

        val book = Firebase.firestore.collection("books").document(bookId)
            .get().await().toObject(Book::class.java)
        val comment = mutableListOf<UserComment>()
        return liveData {
            book?.comment?.forEach {
                val userComment = UserComment()
                userComment.comment = it

                userComment.user = Firebase.firestore.collection("users").document(it.useraId)
                    .get().await().toObject(User::class.java)
                Log.d(TAG, "getComment: ${userComment.comment} , ${userComment.user}")
                comment.add(userComment)
                emit(comment)
            }


        }


    }

    fun addBookRating(bookId: String, ratingBook: RatingBook, userId: String) {

        booksCollectionRef.document(bookId).update("rating", FieldValue.arrayUnion(ratingBook))

    }

    fun deleteBookRating(bookId: String, userId: String) {

        booksCollectionRef.whereEqualTo("bookId", bookId).get().addOnSuccessListener {
            for (document in it) {

                val book = document.toObject(Book::class.java)
                book.rating.forEach {
                    if (it.userId == userId)
                        booksCollectionRef.document(bookId)
                            .update("rating", FieldValue.arrayRemove(it))


                }

            }

        }

    }

    fun deleteBookFav(bookId: String) {

        booksCollectionRef.whereEqualTo("bookId", bookId).get().addOnSuccessListener {
            for (document in it) {

                val user = document.toObject(User::class.java)
                user.favorite.forEach {

                    if (it.bookId == bookId){
                        booksCollectionRef.document(auth.currentUser!!.uid)
                            .update("favorite", FieldValue.arrayRemove(it))

                    }
                }


            }

        }

    }


    suspend fun getFav(bookId: List<Favorite>): LiveData<List<Book>> {
        val books = mutableListOf<Book>()
        return liveData {

            bookId.forEach { fav ->

                booksCollectionRef.document(fav.bookId).get().await().toObject(Book::class.java)
                    ?.let {
                        books.add(
                            it
                        )
                    }
            }

            emit(books)

        }


    }

    suspend fun deleteFav(bookId:String){

        val user = userCollectionRef.document(auth.currentUser!!.uid).get().await().toObject(User::class.java)
       user?.favorite?.forEach {

               booksCollectionRef.document(auth.currentUser!!.uid)
                   .update("favorite", FieldValue.arrayRemove(it))

       }

    }


    companion object {

        private var INSTANT: BookDatabaseRepo? = null

        fun initiliza(context: Context) {

            if (INSTANT == null) {
                INSTANT = BookDatabaseRepo(context)

            }

        }

        fun getInstant(): BookDatabaseRepo =
            INSTANT ?: throw IllegalStateException(" repo has not be init")

    }
}








