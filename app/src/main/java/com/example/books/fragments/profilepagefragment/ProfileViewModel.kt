package com.example.books.fragments.profilepagefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.books.database.DatabaseRepo
import com.example.books.database.User

class ProfileViewModel : ViewModel() {

    val userRepo =DatabaseRepo.getInstant()



    suspend fun getUserData():LiveData<User>{


      return  userRepo.getUserData()

    }


}