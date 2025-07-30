package com.example.todocompose.db

import android.graphics.pdf.models.ListItem
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todocompose.model.TodoItem

@Database(
    entities = [TodoItem::class],
    version = 1
)
abstract class MainDb:RoomDatabase() {
    abstract val dao:Dao
}