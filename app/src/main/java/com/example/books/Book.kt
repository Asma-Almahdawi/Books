package com.example.books

import android.media.Rating
import com.example.books.commentFragment.Comment
import com.example.books.database.RatingBook
import java.util.*
import kotlin.collections.HashMap

data class Book(
    var bookImage:String="",
    var bookOwner:String="",
    var bookName:String="",
    var authorName:String="",
    var yearOfPublication:String="",
    var bookId :String=UUID.randomUUID().toString(),
    val comment :List<Comment> = listOf(),
    val rating: List<RatingBook> = listOf(),
    val summary:String="",
    var pdfFile :String="",


) {
}