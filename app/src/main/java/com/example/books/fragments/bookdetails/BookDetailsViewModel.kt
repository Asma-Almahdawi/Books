package com.example.books.fragments.bookdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.Book
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.Following
import com.example.books.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookDetailsViewModel : ViewModel() {
    private val bookRep = BookDatabaseRepo.getInstant()
    private val userRepo = DatabaseRepo.getInstant()

     fun addComment(comment:Comment , bookId:String){

        return bookRep.addComment(comment , bookId )

    }

    suspend fun getAllBook(): LiveData<List<Book>> {

        return bookRep.getAllBook()


    }

    suspend fun addToFavv(favorite: Favorite , bookId: String){
        userRepo.addToFavv(favorite , bookId)

    }




     fun following(following: Following){


        userRepo.following(following)

    }

    fun rating(){

        bookRep.rating()

    }

    fun addBookRating(bookId: String, ratingBook: RatingBook){

        bookRep.addBookRating(bookId,ratingBook)

    }
    suspend fun getUserData():LiveData<User>{


        return  userRepo.getUserData()

    }

//    suspend fun addToFav(favorite:List<String>){
//
//        userRepo.addToFav(favorite)
//
//    }

    fun getBookRating(bookId:String):List<Float>{
        return bookRep.getBookRating(bookId)
    }

    suspend fun getBook(bookId: String): Book?{

        return bookRep.getBook(bookId)

    }

   fun getComments(bookId:String):LiveData<List<Comment>>{
       var tempList:List<Comment> = emptyList()
       val commentLiveDate:MutableLiveData<List<Comment>> = MutableLiveData()
        viewModelScope.launch(Dispatchers.IO) {
            tempList = bookRep.getAllComment(bookId)
        }.invokeOnCompletion {
            viewModelScope.launch {
                commentLiveDate.value = tempList
            }
        }
       return commentLiveDate
    }
}