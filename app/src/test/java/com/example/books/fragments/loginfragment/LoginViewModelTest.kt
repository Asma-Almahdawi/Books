package com.example.books.fragments.loginfragment

import com.example.books.Book
import com.firebase.ui.auth.ui.email.CheckEmailHandler
import com.google.common.truth.Truth
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    @Before
    fun setUp() {

    loginViewModel= LoginViewModel()

    }
    @Test
    fun checkLogin(email:String , password:String){


        val result = loginViewModel.loginUser("uu@gmail.com" ,"123456" )

        Truth.assertThat(result).isTrue()

    }


}