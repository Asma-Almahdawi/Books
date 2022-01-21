package com.example.books.fragments

import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.AudioBook
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo
import com.example.books.database.Favorite

class AudioBookViewModel : ViewModel() {
    val bookRepo = BookDatabaseRepo.getInstant()
    val userRepo = DatabaseRepo.getInstant()


    fun insertAudioBook(audioBook: AudioBook){


        bookRepo.insertAudioBook(audioBook)

    }

    suspend fun addAudioBookToFavorite(favorite: Favorite, audioBookId: String ) {

        return userRepo.addAudioBookToFavorite(favorite, audioBookId)

    }
}