package com.example.taskreminder.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.taskreminder.dao.TaskDAO
import com.example.taskreminder.database.TaskDatabase
import com.example.taskreminder.model.TaskModel
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDAO: TaskDAO) {


    val allTasks: LiveData<List<TaskModel>> = taskDAO.getTasks()


    suspend fun insert(taskModel: TaskModel) {
        taskDAO.insertTask(taskModel)
    }
}