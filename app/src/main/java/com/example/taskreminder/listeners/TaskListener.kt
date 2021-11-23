package com.example.taskreminder.listeners

import com.example.taskreminder.model.TaskModel

interface TaskListener {
    fun onTaskClicked(task: TaskModel, position: Int)
}