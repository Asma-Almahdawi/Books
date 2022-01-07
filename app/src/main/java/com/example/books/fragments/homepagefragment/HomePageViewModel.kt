package com.example.books.fragments.homepagefragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    private val bookRep = BookDatabaseRepo.getInstant()
    private val database = DatabaseRepo.getInstant()
    var userId: String? = ""

    init {
        viewModelScope.launch {
            userId = getUserUid()
        }
    }
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

    fun deleteBook(book: Book){

        bookRep.deleteBook(book)
    }

   private suspend fun getUserUid():String?{
       return database.getUserData().value?.userId
   }



}