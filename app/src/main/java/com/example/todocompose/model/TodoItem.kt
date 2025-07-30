package com.example.todocompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "main")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val category: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isFavorite: Boolean
)
