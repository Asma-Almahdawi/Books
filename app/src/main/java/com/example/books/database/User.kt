package com.example.books.database

import android.provider.ContactsContract
import com.example.books.Book

data class User(

    var username : String?=null,
    var profileImageUrl: String?= null,
    var books:List<Book> = listOf()


){

}
