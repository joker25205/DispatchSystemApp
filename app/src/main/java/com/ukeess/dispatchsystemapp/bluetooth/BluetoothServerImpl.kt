package com.ukeess.dispatchsystemapp.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.ukeess.dispatchsystemapp.meter.HwMeterPacket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class BluetoothServerImpl(val name: String, val identifier: UUID, val listener: BluetoothServerListener, val type: BluetoothServerType) : BluetoothServer, BluetoothServerSendData {

    private val TAG = "BluetoothController"

    private var mState: Int = 0
    private val STATE_DISCONNECTED = 0
    private val STATE_LISTENING = 1
    private val STATE_CONNECTING = 2
    private val STATE_CONNECTED = 3

    private var mConnectThread: BluetoothServerImpl.ConnectThread? = null
    private var mConnectedThread: BluetoothServerImpl.ConnectedThread? = null

    init {
        setState(STATE_DISCONNECTED)
    }

    @Synchronized
    fun startServer() {
        // destroy currently connection if it exists
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }
        // destroy currently connect if it exists
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }
    }

    @Synchronized
    override fun connectToServer(device: BluetoothDevice) {
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }
        mConnectThread = ConnectThread(device)
        mConnectThread!!.start()
    }


    override fun send(b: ByteArray) {
        if (mConnectedThread != null) {
            mConnectedThread!!.write(b)
        }
    }

    @Synchronized
    fun onConnect(socket: BluetoothSocket) {

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }

        mConnectedThread = ConnectedThread(socket)
        mConnectedThread!!.start()

    }

    private inner class ConnectThread(private val mmDevice: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket?

        init {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            var tmp: BluetoothSocket? = null

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = mmDevice.createRfcommSocketToServiceRecord(identifier)
            } catch (e: IOException) {
                Log.d(TAG, "code 5")
            }

            mmSocket = tmp
            setState(STATE_CONNECTING)
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket!!.connect()
            } catch (connectException: IOException) {

                Log.d(TAG, "code 5")
                try {
                    mmSocket!!.close()
                } catch (closeException: IOException) {
                    Log.d(TAG, "code 6")
                }

                return
            }

            synchronized(this@BluetoothServerImpl) {
                mConnectThread = null
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            onConnect(mmSocket)
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket!!.close()
            } catch (e: IOException) {
                Log.d(TAG, "code 7")
            }

        }
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?
        private var mmBuffer: ByteArray? = null // mmBuffer store for the stream

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
                Log.e(TAG, "> CONNECTED THREAD > Error to get in/out stream$e")
            }

            mmInStream = tmpIn
            mmOutStream = tmpOut
            setState(STATE_CONNECTED)
        }

        override fun run() {
            mmBuffer = ByteArray(1024)
            while (mState == STATE_CONNECTED) {
                try {
                    when (type) {
                        BluetoothServerType.METER_TUNNEL -> {
                            val stx = mmInStream?.read()?.toByte()

                            if (stx == HwMeterPacket.STX) {
                                mmBuffer?.set(0, HwMeterPacket.STX)
                                var id = mmInStream?.read()?.toByte()
                                mmBuffer?.set(1, id!!)
                                val len = mmInStream?.read()?.toInt()

                                for (i in 0 until len!!) {
                                    mmBuffer?.set(3 + i, mmInStream?.read()?.toByte()!!)
                                }
                                val bcc = mmInStream?.read()?.toByte()
                                mmBuffer?.set(3 + len, bcc!!)
                                mmBuffer?.set(3 + len + 1, mmInStream?.read()?.toByte()!!)
                                val packet = HwMeterPacket()
                                if (packet.fromBytesLongData(mmBuffer, len)) {
                                    listener.onDataReceived(packet.toHumanString().toByteArray())
                                }
                            }
                        }
                    }
                } catch (e: IOException) {
                    startServer()
                    break
                }

            }
        }

        fun write(bytes: ByteArray) {
            try {
                mmOutStream!!.write(bytes)

                // Share the sent message with the UI activity.

            } catch (e: IOException) {
                Log.d(TAG, "code 11")
                // Send a failure message back to the activity.
            }

        }

        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            setState(STATE_DISCONNECTED)
        }

    }

    private fun setState(state: Int) {
        when (state) {
            STATE_DISCONNECTED -> log("- STATE_DISCONNECTED")
            STATE_LISTENING -> log("- STATE_LISTENING")
            STATE_CONNECTING -> log("- STATE_CONNECTING")
            STATE_CONNECTED -> log("- STATE_CONNECTED")
        }
        mState = state
    }

    private fun log(log: String) {
        listener.onDataReceived(log.toByteArray())
        Log.d(TAG, log)
    }

}
