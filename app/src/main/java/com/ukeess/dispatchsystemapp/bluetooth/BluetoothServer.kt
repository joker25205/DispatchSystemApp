package com.ukeess.dispatchsystemapp.bluetooth

import android.bluetooth.BluetoothDevice

interface BluetoothServer {
    fun connectToServer(device: BluetoothDevice)
}