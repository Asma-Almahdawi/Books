package com.example.books.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.books.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import kotlin.Exception

private const val TAG = "BookDatabaseRepo"
class BookDatabaseRepo {
    private val database = FirebaseFirestore.getInstance()
    private val booksCollectionRef = Firebase.firestore.collection("books")
    private val bookList = mutableListOf<Book>()

    fun insertBook(book: Book){

        try {

            booksCollectionRef.add(book)
                .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        } catch (e: Exception) {
                Log.d(TAG, " cannot SAVE DATA ")

            }
        }

    fun deleteBook(book: Book){



    }

   suspend fun getAllBook(): LiveData<List<Book>> {

       return liveData {
          val a = booksCollectionRef.get().await().toObjects(Book::class.java)
           emit(a)
       }
    }
//        db.collection("books").get().addOnSuccessListener {
//            for (doc in it){
//                val book = doc.toObject(Book::class.java)
//                bookList.add(book)
//                Log.d(com.example.books.fragments.homepagefragment.TAG," GET DATA $bookList")
//
//            }


    fun updateBookInformation(){



    }




}