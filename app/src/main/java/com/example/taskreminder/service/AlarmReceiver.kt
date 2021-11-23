package com.example.taskreminder.service

import android.R
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskreminder.MainActivity
import kotlin.math.log
import android.content.Intent.getIntent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.example.taskreminder.TestActivity


class AlarmReceiver : BroadcastReceiver() {
    private lateinit var textToSpeechSystem: TextToSpeech

    override fun onReceive(context: Context?, intent: Intent?) {


        val bundle = intent!!.extras
        val title = bundle!!.getString("title")
        val desc = bundle.getString("desc")

        val i = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, i, 0)



        val builder = NotificationCompat.Builder(context!!, "myAlarmReminder")
            .setSmallIcon(R.drawable.ic_btn_speak_now)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(13579, builder.build())


//        val testIntent = Intent(context, TestActivity::class.java)
//        intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(testIntent)


    }
}