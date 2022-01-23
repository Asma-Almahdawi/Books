package com.example.books.fragments.likespagefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.UserRepo
import com.example.books.database.Favorite
import com.example.books.database.User

class LikePageViewModel : ViewModel() {
    private val bookRepo = BookDatabaseRepo.getInstant()
    private val userRepo = UserRepo.getInstant()

    suspend fun getFav(book: List<Favorite>):LiveData<List<Book>>{

        return bookRepo.getFav(book)
    }

    suspend fun deleteFavorite(bookId: String){

        return userRepo.deleteFavorite(bookId)
    }

    suspend fun getUserData():LiveData<User>{

        return  userRepo.getUserData()
    }
}