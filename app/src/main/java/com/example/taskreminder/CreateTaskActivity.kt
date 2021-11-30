package com.example.taskreminder

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.taskreminder.database.TaskDatabase
import com.example.taskreminder.databinding.ActivityCreateTaskBinding
import com.example.taskreminder.model.TaskModel
import com.example.taskreminder.service.AlarmReceiver
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CreateTaskActivity : AppCompatActivity() {


    var mDay = 0
    var mMonth = 0
    var mYear = 0

    var dateTime: String = ""

    //Alarm realated
    var mHour = 0
    var mMinute = 0
    var title = ""


    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var layoutMiscellaneous: LinearLayout

    private var viewSubtitleIndicator: View? = null
    private val layoutWebURL: LinearLayout? = null
    private var alreadyAvailableNote: TaskModel? = null
    private var selectedNoteColor: String? = null
    private var switchVoiceSpeech: SwitchMaterial? = null
    private var switchMarkDone: SwitchMaterial? = null
    private lateinit var textToSpeechSystem: TextToSpeech


    private var dialogDeleteNote: androidx.appcompat.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layoutMiscellaneous = findViewById<LinearLayout>(R.id.layoutMiscellaneous)

        createNotificationChannel()
        calendar = Calendar.getInstance()

        ///////////////////////////
        switchVoiceSpeech = findViewById(R.id.switchSpeech)
        switchMarkDone = findViewById(R.id.switchStatus)
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator)

        selectedNoteColor = "#333333" //default note color.


        if (intent.getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = intent.getSerializableExtra("note") as TaskModel?

            //Hide the keyboard
            window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            )
            setViewOrUpdateNote()

            if (alreadyAvailableNote!!.isVoiceReminder) {
                textToSpeechSystem = TextToSpeech(
                    this
                ) { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        val textToSay = binding.inputNoteTitle.text.toString()
                        textToSpeechSystem.speak(textToSay, TextToSpeech.QUEUE_ADD, null)
                    }
                }
            }


        }


        val deleteBtn = layoutMiscellaneous.findViewById<LinearLayout>(R.id.layoutDeleteNote)



        initMiscellaneous()

        setSubtitleIndicatorColor()


        ///////////////////////////

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
        binding.inputDate.setOnClickListener {

            datePicker()
        }

        binding.inputTime.setOnClickListener {
            MaterialTimePicker()

        }


        binding.imageSave.setOnClickListener {
            if (intent.getBooleanExtra("isViewOrUpdate", false)) {
                updateTask()
            } else {
                saveTask()
                setAlarm()
            }

//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }


    }// onCreate

    private fun updateTask() {
        val title = binding.inputNoteTitle.text.toString()
        val taskDesc = binding.inputNote.text.toString()
        val date = "${binding.inputDate.text.toString()} ${binding.inputTime.text.toString()}"
        val category = selectedNoteColor
        val isVoiceSpeech = switchVoiceSpeech!!.isChecked
        val isCompleted = switchMarkDone!!.isChecked


        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Task Title can't be empty", Toast.LENGTH_SHORT).show()
        } else if (taskDesc.trim().isEmpty()) {
            Toast.makeText(this, "Task Description can't be empty", Toast.LENGTH_SHORT).show()
        } else {

            val taskModel = TaskModel(
                alreadyAvailableNote!!.id,
                title = title,
                description = taskDesc,
                date = date,
                category = category!!,
                isVoiceReminder = isVoiceSpeech,
                isCompleted = isCompleted
            )

            GlobalScope.launch {
                TaskDatabase.getDatabase(this@CreateTaskActivity)
                    .taskDao()
                    .insertTask(taskModel)
            }

        }
    }

    private fun setViewOrUpdateNote() {

        binding.inputNoteTitle.setText(alreadyAvailableNote!!.title)
        binding.inputNote.setText(alreadyAvailableNote!!.description)
        binding.inputTime.setText(alreadyAvailableNote!!.date.substring(11, 22))
        binding.inputDate.setText(alreadyAvailableNote!!.date.substring(0, 10))


        val statusSwitch = layoutMiscellaneous.findViewById<SwitchMaterial>(R.id.switchStatus)
        val statusSpeech = layoutMiscellaneous.findViewById<SwitchMaterial>(R.id.switchSpeech)
        statusSwitch.isChecked = alreadyAvailableNote!!.isCompleted
        statusSpeech.isChecked = alreadyAvailableNote!!.isVoiceReminder

        val gradientDrawable = viewSubtitleIndicator!!.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(alreadyAvailableNote!!.category))

    }


    private fun saveTask() {
        val title = binding.inputNoteTitle.text.toString()
        val taskDesc = binding.inputNote.text.toString()
        val date = dateTime
        val category = selectedNoteColor
        val isVoiceSpeech = switchVoiceSpeech!!.isChecked
        val isCompleted = switchMarkDone!!.isChecked
//
//
//        if (title.trim().isEmpty()) {
//            Toast.makeText(this, "Task Title can't be empty", Toast.LENGTH_SHORT).show()
//        } else if (taskDesc.trim().isEmpty()) {
//            Toast.makeText(this, "Task Description can't be empty", Toast.LENGTH_SHORT).show()
//        } else {
//
//            val taskModel = TaskModel(
//                title = title,
//                description = taskDesc,
//                date = date,
//                category = category!!,
//                isVoiceReminder = isVoiceSpeech,
//                isCompleted = isCompleted
//            )
//
//            GlobalScope.launch {
//                TaskDatabase.getDatabase(this@CreateTaskActivity)
//                    .taskDao()
//                    .insertTask(taskModel)
//            }
//
//
//            setDeviceAlarm(title)
//
//        }
        dismissDeviceAlarm(title)

    }


    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("title", binding.inputNoteTitle.text.toString())
        intent.putExtra("desc", binding.inputNote.text.toString())

        pendingIntent = PendingIntent.getBroadcast(baseContext, 0, intent, Intent.FILL_IN_DATA)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )


//        Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()


    }


    private fun MaterialTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(supportFragmentManager, "pickerFragement")

        picker.addOnPositiveButtonClickListener {

            if (picker.hour > 12) {
                binding.inputTime.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "PM"

                dateTime += binding.inputTime.text.toString()
            } else {
                binding.inputTime.text = String.format("%02d", picker.hour) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + "AM"
                dateTime += binding.inputTime.text.toString()
            }


            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            mHour = picker.hour
            mMinute = picker.minute
            Log.d("titleTag", "MaterialTimePicker: $dateTime")
            Log.d(
                "timetag", "Hour: ${picker.hour}\n" +
                        "Minute: ${picker.minute}"
            )
        }
    }


    private fun datePicker() {
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "${selectedMonth + 1}/$selectedDayOfMonth/$selectedYear"
                binding.inputDate.text = selectedDate
                mMonth = selectedMonth + 1
                mDay = selectedDayOfMonth
                mYear = selectedYear
                dateTime += "$selectedDate   "
            },
            year,
            month,
            day
        )
//        dpd.datePicker.setMaxDate(Date().time)
        dpd.show()
    }


    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }


    fun initMiscellaneous() {

        val bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous)

        layoutMiscellaneous.findViewById<View>(R.id.textMiscellaneous).setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }
        val imageColor1 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor1)
        val imageColor2 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor2)
        val imageColor3 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor3)
        val imageColor4 = layoutMiscellaneous.findViewById<ImageView>(R.id.imageColor4)
        layoutMiscellaneous.findViewById<View>(R.id.viewColor1).setOnClickListener {
            selectedNoteColor = "#333333"
            imageColor1.setImageResource(R.drawable.ic_done)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
            setSubtitleIndicatorColor()
        }
        layoutMiscellaneous.findViewById<View>(R.id.viewColor2).setOnClickListener {
            selectedNoteColor = "#FDBE3B"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(R.drawable.ic_done)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(0)
            setSubtitleIndicatorColor()
        }
        layoutMiscellaneous.findViewById<View>(R.id.viewColor3).setOnClickListener {
            selectedNoteColor = "#FF4842"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(R.drawable.ic_done)
            imageColor4.setImageResource(0)
            setSubtitleIndicatorColor()
        }
        layoutMiscellaneous.findViewById<View>(R.id.viewColor4).setOnClickListener {
            selectedNoteColor = "#3A52FC"
            imageColor1.setImageResource(0)
            imageColor2.setImageResource(0)
            imageColor3.setImageResource(0)
            imageColor4.setImageResource(R.drawable.ic_done)
            setSubtitleIndicatorColor()
        }

        if (alreadyAvailableNote != null && !alreadyAvailableNote!!.category
                .trim().isEmpty()
        ) {
            when (alreadyAvailableNote!!.category) {
                "#FDBE3B" -> layoutMiscellaneous.findViewById<View>(R.id.viewColor2).performClick()
                "#FF4842" -> layoutMiscellaneous.findViewById<View>(R.id.viewColor3).performClick()
                "#3A52FC" -> layoutMiscellaneous.findViewById<View>(R.id.viewColor4).performClick()
            }
        }

        if (alreadyAvailableNote != null) {
            Log.d("deleteTag", "already available note called")

            layoutMiscellaneous.findViewById<View>(R.id.layoutDeleteNote).visibility =
                View.VISIBLE
            layoutMiscellaneous.findViewById<View>(R.id.layoutDeleteNote).setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED


                if (alreadyAvailableNote != null) {
                    layoutMiscellaneous.findViewById<View>(R.id.layoutDeleteNote).visibility =
                        View.VISIBLE
                    layoutMiscellaneous.findViewById<View>(R.id.layoutDeleteNote)
                        .setOnClickListener {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            Log.d("deleteTag", "Delete called")
                            GlobalScope.launch {
                                alreadyAvailableNote?.let { it1 ->
                                    TaskDatabase.getDatabase(applicationContext)
                                        .taskDao()
                                        .deleteTask(it1)
                                }
                            }

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                }
            }
        }
    } // ends layoutMiscellaneous


    private fun showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            Log.d("deleteTag", "showDeleteNoteDialog: not null")
            val builder = AlertDialog.Builder(this)
            val view: View = LayoutInflater.from(this).inflate(
                R.layout.layout_delete_note,
                findViewById<View>(R.id.layoutDeleteNoteContainer) as ViewGroup,
                false
            )
            builder.setView(view)
            dialogDeleteNote = builder.create()
            if (dialogDeleteNote!!.window != null) {
                dialogDeleteNote!!.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            view.findViewById<View>(R.id.textDeleteNote).setOnClickListener {

                GlobalScope.launch {
                    alreadyAvailableNote?.let { it1 ->
                        TaskDatabase.getDatabase(applicationContext)
                            .taskDao()
                            .deleteTask(it1)
                    }
                }
            }


            view.findViewById<View>(R.id.textCancel)
                .setOnClickListener { dialogDeleteNote!!.dismiss() }
        }
        dialogDeleteNote!!.show()
    }


    private fun setSubtitleIndicatorColor() {
        val gradientDrawable = viewSubtitleIndicator!!.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor))
    }


    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TaskReminderChannelId"
            val description = "ChannelForAlarmManager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("myAlarmReminder", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }

    }


    private fun setDeviceAlarm(title: String) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
        intent.putExtra(AlarmClock.EXTRA_HOUR, mHour)
        intent.putExtra(AlarmClock.EXTRA_MINUTES, mMinute)
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, title)
        intent.putExtra(AlarmClock.EXTRA_DAYS, mDay)
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
//        intent.putExtra(AlarmClock.ACTION_DISMISS_ALARM, )
        startActivity(intent)
    }

    private fun dismissDeviceAlarm(title: String) {
        val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM)
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        intent.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, title)
        startActivity(intent)
    }


}