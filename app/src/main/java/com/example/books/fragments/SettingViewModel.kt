package com.example.books.fragments

import androidx.lifecycle.ViewModel
import com.example.books.database.DatabaseRepo

class SettingViewModel : ViewModel() {
    val repo= DatabaseRepo.getInstant()







    fun signOut() = repo.signOut()




}