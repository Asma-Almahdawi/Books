package com.example.books.commentFragment

import java.security.acl.Owner
import java.util.*

data class Comment(
    var useraId: String = "",
    var commentText: String = "",
    var commentId: String = UUID.randomUUID().toString(),
    var username: String = ""

) {


}