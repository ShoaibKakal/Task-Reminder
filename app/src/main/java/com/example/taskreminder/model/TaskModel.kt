package com.example.taskreminder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "task_table")
data class TaskModel (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Int = 0,

    @ColumnInfo(name = "note_title")
    val title: String,

    @ColumnInfo(name = "note_desc")
    val description: String,

    @ColumnInfo(name = "note_date")
    val date: String,

    @ColumnInfo(name = "note_category")
    val category: String,

    @ColumnInfo(name = "note_voiceReminder")
    val isVoiceReminder: Boolean,

    @ColumnInfo(name = "note_isCompleted")
    val isCompleted: Boolean

) : Serializable