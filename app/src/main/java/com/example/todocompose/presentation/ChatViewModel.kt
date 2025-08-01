package com.example.todocompose.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todocompose.model.ChatMessage
import com.example.todocompose.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    // Используем mutableStateOf для Compose-совместимого состояния
    var messageText by mutableStateOf("")
        private set

    // Для списка используем SnapshotStateList
    val messages = mutableStateListOf<ChatMessage>()

    fun sendMessage() {
        if (messageText.isBlank()) return

        viewModelScope.launch {
            try {
                val message = ChatMessage(
                    text = messageText,
                    userId = "current_user_id"  // Замените на реальный ID
                )
                chatRepository.sendMessage(message)
                messages.add(message)
                messageText = ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMessageText(newText: String) {
        messageText = newText
    }
}