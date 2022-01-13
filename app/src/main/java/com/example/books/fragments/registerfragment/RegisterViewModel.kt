package com.example.books.fragments.registerfragment

import androidx.lifecycle.ViewModel
import com.example.books.database.DatabaseRepo

class RegisterViewModel : ViewModel() {
    val repo = DatabaseRepo.getInstant()


}