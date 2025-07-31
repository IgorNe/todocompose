package com.example.todocompose.repository

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat

class BluetoothHidRepository(private val context: Context) {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var hidDevice: BluetoothHidDevice? = null
    private var hostDevice: BluetoothDevice? = null
    private var hidDeviceCallback: HidDeviceCallback? = null

    // HID Report Descriptor for a simple keyboard
    private val reportDescriptor = byteArrayOf(
        0x05.toByte(), 0x01.toByte(), 0x09.toByte(), 0x06.toByte(), 0xA1.toByte(), 0x01.toByte(),
        0x05.toByte(), 0x07.toByte(), 0x19.toByte(), 0xE0.toByte(), 0x29.toByte(), 0xE7.toByte(),
        0x15.toByte(), 0x00.toByte(), 0x25.toByte(), 0x01.toByte(), 0x75.toByte(), 0x01.toByte(),
        0x95.toByte(), 0x08.toByte(), 0x81.toByte(), 0x02.toByte(), 0x95.toByte(), 0x01.toByte(),
        0x75.toByte(), 0x08.toByte(), 0x81.toByte(), 0x03.toByte(), 0x95.toByte(), 0x05.toByte(),
        0x75.toByte(), 0x01.toByte(), 0x05.toByte(), 0x08.toByte(), 0x19.toByte(), 0x01.toByte(),
        0x29.toByte(), 0x05.toByte(), 0x91.toByte(), 0x02.toByte(), 0x95.toByte(), 0x01.toByte(),
        0x75.toByte(), 0x03.toByte(), 0x91.toByte(), 0x03.toByte(), 0x95.toByte(), 0x06.toByte(),
        0x75.toByte(), 0x08.toByte(), 0x15.toByte(), 0x00.toByte(), 0x25.toByte(), 0x65.toByte(),
        0x05.toByte(), 0x07.toByte(), 0x19.toByte(), 0x00.toByte(), 0x29.toByte(), 0x65.toByte(),
        0x81.toByte(), 0x00.toByte(), 0xC0.toByte()
    )

    private val sdpSettings = BluetoothHidDeviceAppSdpSettings(
        "BT Keyboard",
        "Simple BT Keyboard",
        "Android",
        BluetoothHidDevice.SUBCLASS1_KEYBOARD,
        reportDescriptor
    )

    interface ConnectionStateListener {
        fun onConnected(device: BluetoothDevice)
        fun onDisconnected(device: BluetoothDevice)
        fun onError(message: String)
        fun onAppRegistered(success: Boolean)
    }

    private var connectionStateListener: ConnectionStateListener? = null

    fun setConnectionStateListener(listener: ConnectionStateListener) {
        this.connectionStateListener = listener
    }

    init {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
            bluetoothAdapter = bluetoothManager?.adapter

            if (bluetoothAdapter == null) {
                connectionStateListener?.onError("Bluetooth not available")
            }
        } else {
            connectionStateListener?.onError("Bluetooth not supported")
        }
    }

    @SuppressLint("MissingPermission")
    fun initialize() {
        if (!hasBluetoothPermissions()) {
            connectionStateListener?.onError("Bluetooth permissions not granted")
            return
        }

        bluetoothAdapter?.getProfileProxy(
            context,
            object : BluetoothProfile.ServiceListener {
                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                    if (profile == BluetoothProfile.HID_DEVICE) {
                        hidDevice = proxy as BluetoothHidDevice
                        hidDeviceCallback = HidDeviceCallback()
                        hidDevice?.registerApp(
                            sdpSettings,
                            null,
                            null,
                            ContextCompat.getMainExecutor(context),
                            hidDeviceCallback!!
                        )
                    }
                }

                override fun onServiceDisconnected(profile: Int) {
                    if (profile == BluetoothProfile.HID_DEVICE) {
                        hidDevice = null
                    }
                }
            },
            BluetoothProfile.HID_DEVICE
        )
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): List<BluetoothDevice> {
        return if (hasBluetoothPermissions()) {
            bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        if (!hasBluetoothPermissions()) {
            connectionStateListener?.onError("Bluetooth permissions not granted")
            return
        }

        if (hidDevice == null) {
            connectionStateListener?.onError("HID profile not ready")
            return
        }

        hidDevice?.connect(device)
    }

    @SuppressLint("MissingPermission")
    fun sendKey(keyCode: Int) {
        if (hostDevice == null) {
            connectionStateListener?.onError("No connected device")
            return
        }

        if (!hasBluetoothPermissions()) {
            return
        }

        // Key press
        hidDevice?.sendReport(hostDevice, 0, byteArrayOf(0, 0, keyCode.toByte(), 0, 0, 0, 0, 0))
        // Key release
        hidDevice?.sendReport(hostDevice, 0, byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
    }

    @SuppressLint("MissingPermission")
    fun cleanup() {
        if (hasBluetoothPermissions()) {
            hidDevice?.unregisterApp()
            bluetoothAdapter?.closeProfileProxy(BluetoothProfile.HID_DEVICE, hidDevice)
        }
    }

    private fun hasBluetoothPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }

    private inner class HidDeviceCallback : BluetoothHidDevice.Callback() {
        override fun onAppStatusChanged(pluggedDevice: BluetoothDevice?, registered: Boolean) {
            super.onAppStatusChanged(pluggedDevice, registered)
            connectionStateListener?.onAppRegistered(registered)
        }

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChanged(device: BluetoothDevice, state: Int) {
            super.onConnectionStateChanged(device, state)
            when (state) {
                BluetoothProfile.STATE_CONNECTED -> {
                    hostDevice = device
                    connectionStateListener?.onConnected(device)
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    if (hostDevice == device) {
                        hostDevice = null
                        connectionStateListener?.onDisconnected(device)
                    }
                }
            }
        }
    }
}