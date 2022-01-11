package com.example.books.fragments.loginfragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.commentFragment.Validation
import com.example.books.database.DatabaseRepo
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val repo = DatabaseRepo.getInstant()


    fun loginUser(email:String, password:String, context: Context):Boolean{
        var isSuccess = false
        viewModelScope.launch {

         isSuccess = repo.loginUser(email,password,context)
        }
        return isSuccess
    }
}