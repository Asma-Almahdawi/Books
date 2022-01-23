package com.example.books.fragments

import androidx.lifecycle.ViewModel
import com.example.books.database.UserRepo

class SettingViewModel : ViewModel() {
    val repo= UserRepo.getInstant()







    fun signOut() = repo.signOut()




}