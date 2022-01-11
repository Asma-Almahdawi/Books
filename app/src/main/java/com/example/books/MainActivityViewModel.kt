package com.example.books

import androidx.lifecycle.ViewModel
import com.example.books.database.DatabaseRepo

class MainActivityViewModel:ViewModel() {

    val repo = DatabaseRepo.getInstant()

    fun getCurrentUserId():String?{

        return repo.getCurrentUserId()

    }

}