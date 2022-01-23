package com.example.books

import androidx.lifecycle.ViewModel
import com.example.books.database.UserRepo

class MainActivityViewModel:ViewModel() {

    val repo = UserRepo.getInstant()

}