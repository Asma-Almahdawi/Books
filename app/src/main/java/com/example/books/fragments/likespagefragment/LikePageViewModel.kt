package com.example.books.fragments.likespagefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.UserRepo
import com.example.books.database.Favorite
import com.example.books.database.User

class LikePageViewModel : ViewModel() {
    val bookRebo = BookDatabaseRepo.getInstant()
    val userRepo = UserRepo.getInstant()

    suspend fun getFav(book: List<Favorite>):LiveData<List<Book>>{



        return bookRebo.getFav(book)

    }
    suspend fun deleteFavorite(bookId: String){


        return userRepo.deleteFavorite(bookId)

    }

    suspend fun deleteFav(bookId:String){

       bookRebo.deleteFav(bookId)

    }


    suspend fun getUserData():LiveData<User>{


        return  userRepo.getUserData()

    }
    

}