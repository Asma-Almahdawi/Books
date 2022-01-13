package com.example.books.commentFragment

import com.example.books.fragments.loginfragment.LoginFragment
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


class ValidationTest{

private lateinit var validation:Validation
//private lateinit var loginFragment: LoginFragment


@Before
fun setUp(){

    validation =Validation()
//    loginFragment=LoginFragment()
}
@Test
fun checkEmail(){
validation = Validation()
    val result = validation.email("uuyy@gmail.com")

    assertThat(result).isTrue()
}

}