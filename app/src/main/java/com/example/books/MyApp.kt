package com.example.books

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.books.database.BookDatabaseRepo
import com.example.books.database.DatabaseRepo

const val CHANNEL_NOTIFICATION_ID = "HELLO"
class MyApp :Application(){

    override fun onCreate() {
        super.onCreate()

        DatabaseRepo.initiliza(this)
        BookDatabaseRepo.initiliza(this)



        val channelName = resources.getString(R.string.notification_channel_name)

        val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_NOTIFICATION_ID,channelName,
            channelImportance)


        val notificationManager =
            getSystemService(NotificationManager::class.java)

        notificationManager.createNotificationChannel(channel)


}
}