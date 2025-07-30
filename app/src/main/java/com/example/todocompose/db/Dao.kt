package com.example.todocompose.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todocompose.model.TodoItem

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoItem)

    @Delete
    suspend fun delete(item: TodoItem)

    @Query("SELECT * FROM main WHERE category LIKE :category")// передаем переменную категория по ней будет выборка из таблицы
    suspend fun getAllItems(category: String):List<TodoItem>
}