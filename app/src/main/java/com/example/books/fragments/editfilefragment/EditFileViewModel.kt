package com.example.books.fragments.editfilefragment

import androidx.lifecycle.ViewModel
import com.example.books.database.DatabaseRepo
import com.example.books.database.User

class EditFileViewModel : ViewModel() {


    val databaseRepo= DatabaseRepo()

    fun saveUser(user: User){

        databaseRepo.saveUser(user)
    }




}