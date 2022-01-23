package com.example.books.fragments.AudioBookDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.UserComment
import com.example.books.database.*
import kotlinx.coroutines.launch

class AudioBookDetailsViewModel : ViewModel() {
    private val bookRepo = BookDatabaseRepo.getInstant()
    private var userRepo = UserRepo.getInstant()

    suspend fun getAudioBook(audioBookId: String): AudioBook? {

        return bookRepo.getAudioBook(audioBookId)
    }

    fun addAudioBookComment(comment: Comment, audioBookId: String) {

        return bookRepo.addAudioBookComment(comment, audioBookId)
    }

    suspend fun getAudioBookComment(audioBookId: String): LiveData<List<UserComment>> {

        return bookRepo.getAudioBookComment(audioBookId)
    }

    suspend fun getUserData(): LiveData<User> {

        return userRepo.getUserData()
    }


    fun audioBookRating(audioBookId: String, ratingBook: RatingBook, userId: String) {
        viewModelScope.launch {
            bookRepo.audioBookRating(audioBookId, ratingBook, userId)
        }
    }
}
