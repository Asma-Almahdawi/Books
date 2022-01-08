package com.example.books

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.preference.PreferenceManager
import com.example.books.commentFragment.BooksActivity
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo
import java.util.*

const val CHANNEL_NOTIFICATION_ID = "HELLO"
class MyApp :Application(){

    override fun onCreate() {
        super.onCreate()

        DatabaseRepo.initiliza(this)
        BookDatabaseRepo.initiliza(this)

        var change = ""
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("language", "bak")
        if (language == "Turkish") {
            change="tr"
        } else if (language=="English" ) {
            change = "en"
        }else {
            change =""
        }

        BooksActivity.dLocale = Locale(change) //set any locale you want here


        val channelName = resources.getString(R.string.notification_channel_name)

        val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_NOTIFICATION_ID,channelName,
            channelImportance)


        val notificationManager =
            getSystemService(NotificationManager::class.java)

        notificationManager.createNotificationChannel(channel)


}
}