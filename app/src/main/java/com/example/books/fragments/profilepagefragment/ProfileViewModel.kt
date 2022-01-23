package com.example.books.fragments.profilepagefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.AudioBook
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.UserRepo
import com.example.books.database.User

class ProfileViewModel : ViewModel() {
    private val bookRepo = BookDatabaseRepo.getInstant()
   private val userRepo = UserRepo.getInstant()


    suspend fun getUserData(): LiveData<User> {
        return userRepo.getUserData()

    }

    fun getCurrentUserId(): String? {
        return userRepo.getCurrentUserId()

    }

    fun deleteBook(book: Book) {
        bookRepo.deleteBook(book)
    }

    suspend fun getBookFromUserToProfile(): LiveData<List<Book>> {
        return bookRepo.getUserBooks()

    }
}