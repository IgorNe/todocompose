package com.example.todocompose.presentation

import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.AndroidViewModel
import com.example.todocompose.repository.BluetoothHidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BluetoothHidViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BluetoothHidRepository(application)

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val pairedDevices: StateFlow<List<BluetoothDevice>> = _pairedDevices

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        repository.setConnectionStateListener(object : BluetoothHidRepository.ConnectionStateListener {
            override fun onConnected(device: BluetoothDevice) {
                _connectionState.value = ConnectionState.Connected(device)
            }

            override fun onDisconnected(device: BluetoothDevice) {
                _connectionState.value = ConnectionState.Disconnected
            }

            override fun onError(message: String) {
                _errorMessage.value = message
            }

            override fun onAppRegistered(success: Boolean) {
                _connectionState.value = if (success) {
                    ConnectionState.Ready
                } else {
                    ConnectionState.Error("Failed to register HID app")
                }
            }
        })

        repository.initialize()
    }

    fun loadPairedDevices() {
        _pairedDevices.value = repository.getPairedDevices()
    }

    fun connectToDevice(device: BluetoothDevice) {
        repository.connectToDevice(device)
    }

    fun sendKey(keyCode: Int) {
        repository.sendKey(keyCode)
    }

    override fun onCleared() {
        repository.cleanup()
        super.onCleared()
    }

    sealed class ConnectionState {
        object Disconnected : ConnectionState()
        object Ready : ConnectionState()
        data class Connected(val device: BluetoothDevice) : ConnectionState()
        data class Error(val message: String) : ConnectionState()
    }
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
    fun isDeviceConnected(device: BluetoothDevice): Boolean {
        return connectionState.value is ConnectionState.Connected &&
                (connectionState.value as ConnectionState.Connected).device.address == device.address
    }
}