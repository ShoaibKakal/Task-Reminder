package com.example.taskreminder

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.taskreminder.adapter.PageAdapter
import com.example.taskreminder.database.TaskDatabase
import com.example.taskreminder.databinding.ActivityMainBinding
import com.example.taskreminder.listeners.TaskListener
import com.example.taskreminder.model.TaskModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        tabLayoutImp()
        insertTestData()



        binding.imageAddNoteMain.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }


    }


    override fun onStart() {
        super.onStart()
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)

    }
    private fun tabLayoutImp() {
        val tabLayout = binding.categoryTab
        tabLayout.addTab(tabLayout.newTab().setText("Today"))
        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.addTab(tabLayout.newTab().setText("Completed"))

        val pageAdapter = PageAdapter(supportFragmentManager, tabLayout.tabCount)

        binding.viewPager.adapter = pageAdapter
        binding.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }



    private fun insertTestData() {

//        val date: Date = Calendar.getInstance().getTime()
//        val dateFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
//        val strDate: String = dateFormat.format(date)
//
//        val task = TaskModel(
//            1, "Hello title", "just check whats going on over here", strDate, 1,
//            isVoiceReminder = false,
//            isCompleted = true
//        )

//        GlobalScope.launch {
//            TaskDatabase.getDatabase(this@MainActivity)
//                .taskDao()
//                .insertTask(task)
//        }

    }








}