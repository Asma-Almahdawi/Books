package com.example.books.commentFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.books.R

class BooksActivity :AppCompatActivity(){

    companion object{



        fun newIntent(context: Context): Intent {
            return Intent(context , BooksActivity::class.java)
        }




    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}