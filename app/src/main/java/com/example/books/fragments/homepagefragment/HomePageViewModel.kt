package com.example.books.fragments.homepagefragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo
import com.google.firebase.firestore.FirebaseFirestore

class HomePageViewModel : ViewModel() {

    val bookRep = BookDatabaseRepo()
//    private val bookList : LiveData<List<Book>>
//    private val db = FirebaseFirestore.getInstance()

//    fun fetchDataFromFirebase(){
//
//        db.collection("books").get()
//
//
//            .addOnSuccessListener {
//            for (doc in it){
//                val book = doc.toObject(Book::class.java)
//                bookList.add(book)
//
//
//            }
//
////            binding.booksRv.adapter=BookAdapter(bookList)
//
//        }
//    }

    fun insertBook(book: Book){

        bookRep.insertBook(book)

    }

    suspend fun getAllBook(): LiveData<List<Book>> {

      return bookRep.getAllBook()


    }

}