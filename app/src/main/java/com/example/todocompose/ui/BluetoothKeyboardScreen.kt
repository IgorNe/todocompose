package com.example.todocompose.ui

import android.annotation.SuppressLint
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todocompose.presentation.BluetoothHidViewModel
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")

@Composable
fun BluetoothKeyboardScreen(
    viewModel: BluetoothHidViewModel = viewModel()
) {
    val connectionState by viewModel.connectionState.collectAsState()
    val pairedDevices by viewModel.pairedDevices.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Обработка ошибок
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearErrorMessage()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = when (connectionState) {
                    is BluetoothHidViewModel.ConnectionState.Disconnected -> "Disconnected"
                    is BluetoothHidViewModel.ConnectionState.Ready -> "Ready to connect"
                    is BluetoothHidViewModel.ConnectionState.Connected -> "Connected to ${(connectionState as BluetoothHidViewModel.ConnectionState.Connected).device.name}"
                    is BluetoothHidViewModel.ConnectionState.Error -> "Error: ${(connectionState as BluetoothHidViewModel.ConnectionState.Error).message}"
                },
                style = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.height(160.dp))

            Button(
                onClick = { viewModel.loadPairedDevices() },
                enabled = connectionState is BluetoothHidViewModel.ConnectionState.Ready
            ) {
                Text(
                    "Show Paired Devices",
                    style = TextStyle(color = Color.Black)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)  // Добавляем отступы
            ) {
                items(pairedDevices) { device ->
                    DeviceItem(
                        device = device,
                        isConnected = viewModel.isDeviceConnected(device),
                        onClick = { viewModel.connectToDevice(device) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { viewModel.sendKey(0x14) }) { // Q key
                    Text("Q")
                }
                Button(onClick = { viewModel.sendKey(0x1A) }) { // W key
                    Text("W")
                }
            }
        }

        // Snackbar внизу экрана
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}