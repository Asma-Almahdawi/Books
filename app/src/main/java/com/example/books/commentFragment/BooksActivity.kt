package com.example.books.commentFragment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.example.books.R
import java.util.*

class BooksActivity :AppCompatActivity(){

    companion object{



            var dLocale: Locale? = null

        fun newIntent(context: Context): Intent {
            return Intent(context , BooksActivity::class.java)

        }
        init {
//        initupdateConfig(this)
        }
        fun updateConfig(wrapper: ContextThemeWrapper) {
            if(dLocale== Locale("") ) // Do nothing if dLocale is null
                return

            Locale.setDefault(dLocale)
            val configuration = Configuration()
            configuration.setLocale(dLocale)
            wrapper.applyOverrideConfiguration(configuration)
        }

        }




        fun newIntent(context: Context): Intent {
            return Intent(context , BooksActivity::class.java)
        }









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}