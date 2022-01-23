package com.example.books.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.database.*

class AudioBookViewModel : ViewModel() {
    val bookRepo = BookDatabaseRepo.getInstant()
    val userRepo = UserRepo.getInstant()


    fun insertAudioBook(audioBook: AudioBook){


        bookRepo.insertAudioBook(audioBook)

    }

    suspend fun addAudioBookToFavorite(favorite: Favorite, audioBookId: String ) {

        return userRepo.addAudioBookToFavorite(favorite, audioBookId)

    }

    suspend fun getUserData(): LiveData<User> {


        return  userRepo.getUserData()

    }
}