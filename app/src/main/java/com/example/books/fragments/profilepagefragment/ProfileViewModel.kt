package com.example.books.fragments.profilepagefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo
import com.example.books.database.User

class ProfileViewModel : ViewModel() {
  val bookRepo =BookDatabaseRepo.getInstant()
    val userRepo =DatabaseRepo.getInstant()



    suspend fun getUserData():LiveData<User>{


      return  userRepo.getUserData()

    }

    suspend fun getBookFromUser(userId:String):LiveData<List<Book>>{

        return userRepo.getBookFromUser(userId)
    }

    suspend fun getBookFromUserToProfile():LiveData<List<Book>>{

        return bookRepo.getUserBooks()

    }

  suspend fun followers(followers:String , userId: String){

    return userRepo.followers(followers , userId)

  }


}