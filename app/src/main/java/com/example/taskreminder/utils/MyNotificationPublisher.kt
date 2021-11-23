package com.example.taskreminder.utils

import android.app.Notification
import android.content.BroadcastReceiver
import android.app.NotificationManager
import android.content.Context

import android.content.Intent




class MyNotificationPublisher: BroadcastReceiver() {
    public var NOTIFICATION_ID = "shoaib_notification_id"
    public var NOTIFICATION = "notification_1234"

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: Notification? = intent.getParcelableExtra(NOTIFICATION)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(notificationId, notification)
    }
}