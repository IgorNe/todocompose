package com.example.todocompose.di

import android.app.Application
import androidx.room.Room
import com.example.todocompose.db.MainDb
import com.example.todocompose.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideMainDb(app: Application):MainDb{
        return Room.databaseBuilder(
            app,
            MainDb::class.java,
            "notes.db"
        )
            //.createFromAsset("db/notes.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideChatRepository(): ChatRepository = ChatRepository()
}

