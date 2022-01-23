package com.example.books.fragments

import androidx.lifecycle.ViewModel
import com.example.books.database.UserRepo

class SettingViewModel : ViewModel() {
    private val userRepo= UserRepo.getInstant()

    fun signOut() = userRepo.signOut()
}