package com.example.books.commentFragment

import android.provider.ContactsContract

class Validation {

private val EMAILE_PATTREN ="[a-zA-Z-9._-]+@[a-z]+"
    fun email(email:String):Boolean{

        if(email.matches(EMAILE_PATTREN.toRegex()))

            return true
        return false
    }


}