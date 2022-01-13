package com.example.books.fragments.registerfragment

import com.example.books.commentFragment.Constants
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest{

@Test
fun `empty password or email return please enter email or password`(){

    val result = RegistrationUtil.validateRegistrationInput(
        "",
        "123456"

    )

    assertThat(result).isEqualTo(Constants.usernameOrPasswordIsEmpty)

}
    @Test
    fun `valid email and correctly repeated password return true`(){

        val result = RegistrationUtil.validateRegistrationInput(
            "uu@gmail.com",
            "123456"

        )

        assertThat(result).isEmpty()


    }


}