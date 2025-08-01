package com.example.todocompose.repository

import com.example.todocompose.model.ChatMessage
import com.example.todocompose.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.github.jan.supabase.storage.storage



class ChatRepository {
    private val client = SupabaseClient.client

    // Получить все сообщения
    suspend fun getMessages(): List<ChatMessage> {
        return client.postgrest["messages"]
            .select()
            .decodeList<ChatMessage>()
    }

    // Отправить сообщение
    suspend fun sendMessage(message: ChatMessage) {
        client.postgrest["messages"]
            .insert(message)  // Просто передаем объект
    }


    // Загрузить фото (возвращает URL файла)
    suspend fun uploadImage(file: ByteArray, fileName: String): String {
        val bucket = client.storage["chat_images"]

        // Use the proper upload function with options
        bucket.upload(
            path = fileName,
            data = file,
            options = {
                //contentType("image/jpeg") // or "image/png" depending on your file type
                upsert = true
            }
        )

        return bucket.publicUrl(fileName)
    }
}