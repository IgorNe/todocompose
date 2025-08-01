package com.example.todocompose.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.MaterialTheme
import com.example.todocompose.presentation.ChatViewModel
import io.ktor.util.collections.getValue
import io.ktor.utils.io.InternalAPI


@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel()
) {
    // Правильное чтение состояний из ViewModel
    val messageText = viewModel.messageText
    val messages = viewModel.messages

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Text(
                    text = message.text,
                    modifier = Modifier.padding(8.dp)
                )
                Divider()
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = { viewModel.updateMessageText(it) },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { viewModel.sendMessage() },
                enabled = messageText.isNotBlank()
            ) {
                Text("Отправить")
            }
        }
    }
}

// Preview для Android Studio
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    MaterialTheme {
        ChatScreen()
    }
}