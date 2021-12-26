package com.example.books

import com.example.books.commentFragment.Comment
import java.util.*

data class Book(
//    val bookFile:String="",
    var bookImage:String="",
    var bookOwner:String="",

    var bookName:String="",
    var pdfFile :String="",
    var authorName:String="",
    var yearOfPublication:String="",
    var bookId :String="",
    val comment :List<Comment> = listOf()
//    val numberOfPages:String = ""

) {
}