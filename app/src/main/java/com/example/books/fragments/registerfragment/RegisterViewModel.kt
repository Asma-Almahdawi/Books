package com.example.books.fragments.registerfragment

import androidx.lifecycle.ViewModel
import com.example.books.database.UserRepo

class RegisterViewModel : ViewModel() {
    val repo = UserRepo.getInstant()


}