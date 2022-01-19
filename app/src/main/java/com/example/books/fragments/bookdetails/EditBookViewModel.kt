package com.example.books.fragments.bookdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo
import kotlinx.coroutines.tasks.await

class EditBookViewModel : ViewModel() {
   val bookRepo=BookDatabaseRepo.getInstant()


    suspend fun getBookData(bookId:String): LiveData<Book> {
       return bookRepo.getBookData(bookId)
    }
    suspend fun getBook(bookId: String): Book? {

       return bookRepo.getBook(bookId)


    }
    fun updateBook(book:Book,
                   bookImage:String ,
                   bookName:String,
                   authorName:String,
                   yearOfPublication:String,
                   pdfFile :String){

        return bookRepo.updateBook(bookImage = bookImage,book = book ,
           bookName= bookName,
            authorName=authorName,
            yearOfPublication=yearOfPublication,
            pdfFile=pdfFile )
    }

    fun insertBook(book: Book){


        bookRepo.insertBook(book)

    }


}