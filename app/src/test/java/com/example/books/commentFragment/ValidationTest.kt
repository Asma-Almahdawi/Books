package com.example.books.commentFragment

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class ValidationTest{

private lateinit var validation:Validation

fun setUp(){

    validation =Validation()
}
@Test
fun checkEmail(){
validation = Validation()
    val result = validation.email("uuyy@gmail.com")

    assertThat(result).isTrue()
}

}