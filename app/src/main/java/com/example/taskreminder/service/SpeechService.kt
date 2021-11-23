package com.example.taskreminder.service
//
//import android.app.Service
//import android.app.Service.START_NOT_STICKY
//import android.content.Intent
//import android.os.Handler
//import android.os.IBinder
//import android.speech.tts.TextToSpeech
//import java.util.*
//
//class SpeechService: Service(), TextToSpeech.OnInitListener {
//
//    val EXTRA_WORD = "word"
//    val EXTRA_MEANING = "meaning"
//
//    private var tts: TextToSpeech? = null
//    private var word: String? = null
//    private  var meaning: String? = null
//    private var isInit = false
//    private var handler: Handler? = null
//
//
//    override fun onCreate() {
//        super.onCreate()
//        tts = TextToSpeech(applicationContext, this)
//        handler = Handler()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        handler!!.removeCallbacksAndMessages(null)
//
//        word = intent!!.getStringExtra(EXTRA_WORD)
//        meaning = intent.getStringExtra(EXTRA_MEANING)
//
//        if (isInit) {
//            speak()
//        }
//
//        handler!!.postDelayed({ stopSelf() }, (15 * 1000).toLong())
//
//        (return SpeechService)
//    }
//
//    override fun onDestroy() {
//        if (tts != null) {
//            tts!!.stop();
//            tts!!.shutdown();
//        }
//        super.onDestroy();
//    }
//
//    private fun speak() {
//        if (tts != null) {
//            tts!!.speak(word, TextToSpeech.QUEUE_FLUSH, null);
//            tts!!.speak(meaning, TextToSpeech.QUEUE_ADD, null);
//        }    }
//
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onInit(status: Int) {
//        if (status === TextToSpeech.SUCCESS) {
//            val result = tts!!.setLanguage(Locale.US)
//            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
//                speak()
//                isInit = true
//            }
//        }
//    }
//}