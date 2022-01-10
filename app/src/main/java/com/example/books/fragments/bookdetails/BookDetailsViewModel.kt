package com.example.books.fragments.bookdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.Following
import com.example.books.commentFragment.UserComment
import com.example.books.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookDetailsViewModel : ViewModel() {
    private val bookRep = BookDatabaseRepo.getInstant()
    private val userRepo = DatabaseRepo.getInstant()

     fun addComment(comment:Comment , bookId:String){

        return bookRep.addComment(comment , bookId )

    }


    suspend fun addToFavv(favorite: Favorite , bookId: String){
        userRepo.addToFavv(favorite , bookId)

    }
    fun deleteRating(bookId: String, userId: String){

        return bookRep.deleteBookRating(bookId,userId)

    }





    fun addBookRating(bookId: String, ratingBook: RatingBook , userId: String){

        bookRep.addBookRating(bookId,ratingBook , userId)

    }




   suspend fun getComment(bookId: String ):LiveData<List<UserComment>>{


       return bookRep.getComment(bookId)

   }


    suspend fun getBook(bookId: String): Book?{

        return bookRep.getBook(bookId)

    }


}