package com.example.books.fragments

import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo

class BooksViewModel : ViewModel() {

    val bookRepo = BookDatabaseRepo()


    fun insertBook(book: Book){


        bookRepo.insertBook(book)

    }
}