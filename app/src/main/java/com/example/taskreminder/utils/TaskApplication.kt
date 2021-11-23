package com.example.taskreminder.utils

import android.app.Application
import com.example.taskreminder.database.TaskDatabase
import com.example.taskreminder.repositories.TaskRepository

class TaskApplication: Application() {

    val database by lazy { TaskDatabase.getDatabase(this) }
    val repository by lazy { TaskRepository(database.taskDao()) }
}