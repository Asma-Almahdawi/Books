package com.example.books.fragments.editfilefragment

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.database.DatabaseRepo
import com.example.books.database.User
import kotlinx.coroutines.launch

class EditFileViewModel : ViewModel() {


    val databaseRepo= DatabaseRepo.getInstant()

    fun saveUser(user: User){

        databaseRepo.saveUser(user)
    }

    suspend fun getUserData():LiveData<User>{

      return  databaseRepo.getUserData()

    }

     fun uploadProfileImage(curFile:Uri):Boolean{
        var isSuccess = false
        viewModelScope.launch {
            isSuccess = databaseRepo.uploadImage(curFile)
        }
        return isSuccess

    }

    fun savaProfileUserData(user: User){

        databaseRepo.savaProfileUserData(user)


    }


}