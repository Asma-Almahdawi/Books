package com.example.books.commentFragment


class Validation {

    private val EMAILE_PATTREN = "[a-zA-Z-9._-]+@[a-z]+\\.+[a-z]+"
    fun email(email: String): Boolean {

        if (email.matches(EMAILE_PATTREN.toRegex()))

            return true
        return false
    }


}