package com.example.books.fragments.homepagefragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.Book
import com.example.books.database.AudioBook
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.UserRepo
import com.example.books.database.User
import kotlinx.coroutines.launch

class HomePageViewModel : ViewModel() {

    private val bookRep = BookDatabaseRepo.getInstant()
    private val database = UserRepo.getInstant()
    var userId: String? = ""

    init {
        viewModelScope.launch {
            userId = getUserUid()
        }
    }

   suspend fun getAllBook(): LiveData<List<Book>> {

      return bookRep.getAllBook()


    }
    suspend fun getAllAudioBook(): LiveData<List<AudioBook>> {

        return bookRep.getAllAudioBook()
    }

    fun getCurrentUserId():String?{

        return database.getCurrentUserId()
    }

   private suspend fun getUserUid():String?{

       return database.getUserData().value?.userId
   }

    suspend fun searchBookName(letter:String):LiveData<List<Book>>{

        return bookRep.searchBookName(letter)
    }
}