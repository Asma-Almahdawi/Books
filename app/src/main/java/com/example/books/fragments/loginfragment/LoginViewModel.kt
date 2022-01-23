package com.example.books.fragments.loginfragment

import androidx.lifecycle.ViewModel
import com.example.books.database.UserRepo

class LoginViewModel : ViewModel() {

    val userRepo = UserRepo.getInstant()


    suspend fun loginUser(email: String, password: String): Boolean {

        return userRepo.loginUser(email, password)
    }
}