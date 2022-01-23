package com.example.books.fragments.bookdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.UserComment
import com.example.books.database.*
import kotlinx.coroutines.launch

class BookDetailsViewModel : ViewModel() {
    private val bookRep = BookDatabaseRepo.getInstant()
    private val userRepo = UserRepo.getInstant()

    fun addComment(comment: Comment, bookId: String) {

        return bookRep.addComment(comment, bookId)
    }

    suspend fun addToFavv(favorite: Favorite, bookId: String) {
        userRepo.addToFavv(favorite, bookId)
    }

    fun addBookRating(bookId: String, ratingBook: RatingBook, userId: String) {
        viewModelScope.launch {
            bookRep.addBookRating(bookId, ratingBook, userId)
        }
    }

    suspend fun getComment(bookId: String): LiveData<List<UserComment>> {
        return bookRep.getComment(bookId)

    }
    suspend fun getBook(bookId: String): Book? {

        return bookRep.getBook(bookId)

    }

    suspend fun deleteFavorite(bookId: String) {


        return userRepo.deleteFavorite(bookId)

    }

    suspend fun getUserData(): LiveData<User> {


        return userRepo.getUserData()

    }


}