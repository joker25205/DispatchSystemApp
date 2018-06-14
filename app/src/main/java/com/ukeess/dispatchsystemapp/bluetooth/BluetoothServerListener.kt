package com.ukeess.dispatchsystemapp.bluetooth

interface BluetoothServerListener {
    fun onDataReceived(data : ByteArray)
}