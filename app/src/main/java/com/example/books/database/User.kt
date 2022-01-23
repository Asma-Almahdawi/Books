package com.example.books.database

import android.provider.ContactsContract
import com.example.books.Book
import com.example.books.commentFragment.Following

data class User(
    var userId:String="",
    var username: String="",
    var bio:String="",
    var profileImageUrl: String?= "",
    var books:List<Book> = listOf(),
    var flowers:List<String> = listOf(),
    var following: List<String> = listOf(),
    var favorite:List<Favorite> = listOf()
)
