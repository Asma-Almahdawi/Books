package com.example.books.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.database.*

class AudioBookViewModel : ViewModel() {
    private val bookRepo = BookDatabaseRepo.getInstant()
   private val userRepo = UserRepo.getInstant()

    fun insertAudioBook(audioBook: AudioBook){
        bookRepo.insertAudioBook(audioBook)

    }

    suspend fun getUserData(): LiveData<User> {
        return  userRepo.getUserData()

    }
}