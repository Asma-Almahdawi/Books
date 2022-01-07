package com.example.books

import android.app.Application
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo

class MyApp :Application(){

    override fun onCreate() {
        super.onCreate()

        DatabaseRepo.initiliza(this)
        BookDatabaseRepo.initiliza(this)
    }

}