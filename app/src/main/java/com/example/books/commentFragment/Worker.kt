package com.example.books.commentFragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.books.CHANNEL_NOTIFICATION_ID
import com.example.books.R

class Worker(private val context: Context, workerPrams: WorkerParameters)
    : Worker(context , workerPrams)  {
    override fun doWork(): Result {

        val notificationMsg = inputData.getString("welcome we have new book")
        createNotification("welcome" , notificationMsg.toString())


        return Result.success()

    }

    private fun createNotification(title:String , description: String){

                val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(NOTIFICATIN , "notify" ,NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)
        val resources = context.resources
        val notification = NotificationCompat.Builder(
            context, CHANNEL_NOTIFICATION_ID
        ).setTicker(resources.getString(R.string.new_books_title))
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(resources.getString(R.string.new_books_title))
            .setContentText(resources.getString(R.string.new_books_text))
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }



//
//        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
//            val notificationChannel = NotificationChannel(NOTIFICATIN , "notify" ,NotificationManager.IMPORTANCE_DEFAULT)
//        notificationManager
//
//        val intent = BooksActivity.newIntent(context)
//        val pendingIntent = PendingIntent.getActivity(
//            context, 0,
//            intent, PendingIntent.FLAG_IMMUTABLE
//        )
//
//
//
//        val resources = context.resources
//        val notification = NotificationCompat.Builder(
//            context, CHANNEL_NOTIFICATION_ID
//        ).setTicker(resources.getString(R.string.new_books_title))
//            .setSmallIcon(R.drawable.notification)
//            .setContentTitle(resources.getString(R.string.new_books_title))
//            .setContentText(resources.getString(R.string.new_books_text))
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .build()
//
////
//        showBackgroundNotification(notification)
//
//        return Result.success()
//    }
//fun showBackgroundNotification(notification: Notification){
//    val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
//        putExtra(NOTIFICATIN , notification)
//    }
//
//    context.sendOrderedBroadcast(intent , PERM_PRIVATE)
//}
//
//
companion object{
    const val ACTION_SHOW_NOTIFICATION = "com.example.books.SHOW_NOTIFICATION"
    const val PERM_PRIVATE = "com.example.books.PRIVATE"

    const val NOTIFICATIN = "notify"
}}
