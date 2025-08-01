package com.example.todocompose.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: Long? = null,  // Автоинкремент в Supabase
    val text: String,
    @SerialName("user_id") val userId: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("audio_url") val audioUrl: String? = null,
    @SerialName("created_at") val createdAt: String? = null // Можно использовать Instant
)