package com.example.books.fragments.bookdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.database.BookDatabaseRepo
import java.util.*

class BookDetailsViewModel : ViewModel() {
    private val bookRep = BookDatabaseRepo()


     fun addComment(comment:Comment , bookId:String){

        return bookRep.addComment(comment , bookId )

    }

    fun getBook(bookId: String){

        return bookRep.getBook(bookId)

    }
}