package com.example.books.fragments.bookdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.database.BookDatabaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class BookDetailsViewModel : ViewModel() {
    private val bookRep = BookDatabaseRepo()


     fun addComment(comment:Comment , bookId:String){

        return bookRep.addComment(comment , bookId )

    }

    suspend fun getAllBook(): LiveData<List<Book>> {

        return bookRep.getAllBook()


    }

    fun getBook(bookId: String){

        return bookRep.getBook(bookId)

    }

   fun getComments(bookId:String):LiveData<List<Comment>>{
       var tempList:List<Comment> = emptyList()
       val commentLiveDate:MutableLiveData<List<Comment>> = MutableLiveData()
        viewModelScope.launch(Dispatchers.IO) {
            tempList = bookRep.getAllComment(bookId)
        }.invokeOnCompletion {
            viewModelScope.launch {
                commentLiveDate.value = tempList
            }
        }
       return commentLiveDate
    }
}