package com.ukeess.dispatchsystemapp.bluetooth

import android.graphics.Color
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
import com.ukeess.dispatchsystemapp.R
import com.ukeess.dispatchsystemapp.adapters.LogsAdapter
import com.ukeess.dispatchsystemapp.enums.LogType
import com.ukeess.dispatchsystemapp.meter.HwMeterPacket
import com.ukeess.dispatchsystemapp.model.LogInfo
import com.ukeess.dispatchsystemapp.utils.SeparatorDecoration
import com.ukeess.dispatchsystemapp.utils.getCurrentTime
import kotlinx.android.synthetic.main.fragment_meter_tunnel.*


class MeterTunnelFragment : Fragment(), BluetoothServerListener {

    private val MESSAGE: Int = 0
    private val handler: Handler

    var logsList = ArrayList<LogInfo>()
    var adapter: LogsAdapter? = null

    init {
        handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (adapter != null) {
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }


    private var dataSender: DataSender? = null

    fun setDataSender(dataSender: DataSender) {
        this.dataSender = dataSender
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tunnelMessageList.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        tunnelMessageList.addItemDecoration(SeparatorDecoration(context!!, Color.GRAY, 1.5f))
        adapter = LogsAdapter(context!!, logsList)
        tunnelMessageList.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_meter_tunnel, container, false)
    }


    override fun onDataReceived(data: ByteArray) {
        Log.d("Tag", String(data))
        var data = LogInfo(String(data), LogType.RESPONSE, getCurrentTime())
        logsList.add(0, data)


        dataSender?.let {
            val meterPack = HwMeterPacket(HwMeterPacket.ID_ACK)
            it.send(meterPack.toBytes())
            data = LogInfo(String(meterPack.toBytes()), LogType.REQUEST, getCurrentTime())
            logsList.add(0, data)
        }

        val m = handler.obtainMessage(MESSAGE)
        handler.sendMessage(m)
    }


}
