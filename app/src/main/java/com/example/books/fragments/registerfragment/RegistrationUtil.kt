package com.example.books.fragments.registerfragment

import com.example.books.commentFragment.Constants

object RegistrationUtil {


    fun validateRegistrationInput(

        email: String,
        password: String
    ): String {

        if (email.isEmpty() || password.isEmpty()) {
            return Constants.usernameOrPasswordIsEmpty
        }
        return "you are logged"
    }
}