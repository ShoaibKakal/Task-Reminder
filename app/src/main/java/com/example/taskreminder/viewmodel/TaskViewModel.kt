package com.example.taskreminder.viewmodel

import androidx.lifecycle.*
import com.example.taskreminder.model.TaskModel
import com.example.taskreminder.repositories.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {


    val allWords: LiveData<List<TaskModel>> = repository.allTasks

    fun insert(task: TaskModel) = viewModelScope.launch {
        repository.insert(task)
    }

    class TaskViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {

                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T

            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }

    }
}