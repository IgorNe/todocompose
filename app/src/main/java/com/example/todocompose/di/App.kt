package com.example.todocompose.di

import android.app.Application
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.todocompose.model.TodoItem
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application()