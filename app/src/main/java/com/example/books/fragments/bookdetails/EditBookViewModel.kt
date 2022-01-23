package com.example.books.fragments.bookdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo

class EditBookViewModel : ViewModel() {
    private val bookRepo = BookDatabaseRepo.getInstant()

    suspend fun getBook(bookId: String): Book? {
        return bookRepo.getBook(bookId)
    }

    fun updateBook(
        book: Book,
        bookName: String,
        authorName: String,
        yearOfPublication: String,
    )
    {
        return bookRepo.updateBook(
            book = book,
            bookName = bookName,
            authorName = authorName,
            yearOfPublication = yearOfPublication,
        )
    }
}