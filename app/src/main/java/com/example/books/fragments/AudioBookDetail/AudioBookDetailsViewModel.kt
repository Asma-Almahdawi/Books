package com.example.books.fragments.AudioBookDetail

import androidx.lifecycle.ViewModel
import com.example.books.commentFragment.Comment
import com.example.books.database.AudioBook
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo

class AudioBookDetailsViewModel : ViewModel() {
   private val bookRepo = BookDatabaseRepo.getInstant()
    private var userRepo= DatabaseRepo.getInstant()


    suspend fun getAudioBook(audioBookId: String): AudioBook? {

        return bookRepo.getAudioBook(audioBookId)
    }

    fun getCurrentUserId(): String? {

        return userRepo.getCurrentUserId()
    }

    fun addAudioBookComment(comment: Comment, audioBookId: String) {

        return bookRepo.addAudioBookComment(comment,audioBookId)
    }
}