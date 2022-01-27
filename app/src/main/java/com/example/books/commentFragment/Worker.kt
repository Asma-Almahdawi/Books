package com.example.books.commentFragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.books.CHANNEL_NOTIFICATION_ID
import com.example.books.R

class Worker(private val context: Context, workerPrams: WorkerParameters) :
    Worker(context, workerPrams) {
    override fun doWork(): Result {

        val notificationMsg = inputData.getString("welcome to Books app")
        createNotification("welcome", notificationMsg.toString())
        return Result.success()
    }

    private fun createNotification(title: String, description: String) {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel =
            NotificationChannel(NOTIFICATIN, "notify", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)
        val resources = context.resources
        val notification = NotificationCompat.Builder(
            context, CHANNEL_NOTIFICATION_ID
        ).setTicker(resources.getString(R.string.welcome))
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(resources.getString(R.string.welcome))
            .setContentText(resources.getString(R.string.new_notify))
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val NOTIFICATIN = "notify"
    }
}
