package com.example.books

import android.media.Rating
import com.example.books.commentFragment.Comment
import com.example.books.database.RatingBook
import java.util.*
import kotlin.collections.HashMap

data class Book(
//    val bookFile:String="",
    var bookImage:String="",
    var bookOwner:String="",

    var bookName:String="",
    var pdfFile :String="",
    var authorName:String="",
    var yearOfPublication:String="",
    var bookId :String="",
    val comment :List<Comment> = listOf(),
//    val rating: MutableMap<String, Float> = mutableMapOf(),
    val rating: List<RatingBook> = listOf(),
    var value :String=""
//    val numberOfPages:String = ""

) {
}