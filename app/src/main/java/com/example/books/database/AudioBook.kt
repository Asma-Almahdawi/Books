package com.example.books.database

import com.example.books.commentFragment.Comment
import java.util.*

data class AudioBook(

    var audioBookId:String= UUID.randomUUID().toString(),
    var bookImage:String="",
    var bookOwner:String="",
    var bookName:String="",
    var authorName:String="",
    var yearOfPublication:String="",
    val comment :List<Comment> = listOf(),
    val rating: List<RatingBook> = listOf(),
    val summary:String="",
    var audioFile:String = ""

    )