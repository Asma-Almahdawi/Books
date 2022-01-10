package com.example.books.fragments.profilepagefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.Book
import com.example.books.database.DatabaseRepo
import com.example.books.database.User

class ProfileViewModel : ViewModel() {

    val userRepo =DatabaseRepo.getInstant()



    suspend fun getUserData():LiveData<User>{


      return  userRepo.getUserData()

    }

    suspend fun getBookFromUser(userId:String):LiveData<List<Book>>{

        return userRepo.getBookFromUser(userId)
    }


}