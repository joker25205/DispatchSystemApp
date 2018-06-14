package com.ukeess.dispatchsystemapp.bluetooth

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_meter_tunnel.*
import com.ukeess.dispatchsystemapp.R
import com.ukeess.dispatchsystemapp.adapters.LogsAdapter
import com.ukeess.dispatchsystemapp.bluetooth.BluetoothServerListener
import com.ukeess.dispatchsystemapp.bluetooth.BluetoothServerSendData
import com.ukeess.dispatchsystemapp.meter.HwMeterPacket


class MeterTunnelFragment : Fragment(), BluetoothServerListener {

    private val MESSAGE: Int = 0
    private val mHandler: Handler

    var logsList = ArrayList<String>()
    var adapter: LogsAdapter? = null
    var checkState: Boolean = false

    init {
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val message: String = msg.obj.toString()
                logsList.add(0, message)
                if (adapter != null) {
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }


    private var mBluetoothServerSendData: BluetoothServerSendData? = null

    fun setBluetoothServerSendData(bluetoothServerSendData: BluetoothServerSendData) {
        mBluetoothServerSendData = bluetoothServerSendData
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tunnelMessageList.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        adapter = LogsAdapter(context!!, logsList)
        tunnelMessageList.adapter = adapter
        checkState = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meter_tunnel, container, false)
    }


    override fun onDataReceived(data: ByteArray) {
        Log.d("Tag", String(data))
        val m = mHandler.obtainMessage(MESSAGE, String(data))
        mHandler.sendMessage(m)

        if (mBluetoothServerSendData != null)
            mBluetoothServerSendData?.send(HwMeterPacket(HwMeterPacket.ID_ACK).toBytes())
    }


}
