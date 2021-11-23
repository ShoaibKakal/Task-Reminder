package com.example.taskreminder.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskreminder.model.TaskModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskModel)

    @Delete
    suspend fun deleteTask(task: TaskModel)

    @Query("SELECT * FROM task_table ORDER BY note_id DESC")
    fun getTasks(): LiveData<List<TaskModel>>
}