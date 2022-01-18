package com.example.books.fragments

import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.AudioBook
import com.example.books.database.BookDatabaseRepo

class AudioBookViewModel : ViewModel() {
    val bookRepo = BookDatabaseRepo.getInstant()


    fun insertAudioBook(audioBook: AudioBook){


        bookRepo.insertAudioBook(audioBook)

    }
}