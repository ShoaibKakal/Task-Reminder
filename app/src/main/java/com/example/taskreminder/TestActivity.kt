package com.example.taskreminder


import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskreminder.databinding.ActivityTestBinding
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.*
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Toast


class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar: Calendar
    private lateinit var picker: MaterialTimePicker
    private lateinit var textToSpeechSystem: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        textToSpeechSystem = TextToSpeech(
            this
        ) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val textToSay = "Hello world, this is a test message sending to Ghora!"
                textToSpeechSystem.speak(textToSay, TextToSpeech.QUEUE_ADD, null)
                Toast.makeText(this,"runnning", Toast.LENGTH_SHORT).show()
            }
        }
    }




}














