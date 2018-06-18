package com.ukeess.dispatchsystemapp.bluetooth


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.ukeess.dispatchsystemapp.R
import com.ukeess.dispatchsystemapp.adapters.DeviceListAdapter
import com.ukeess.dispatchsystemapp.utils.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_bluetooth_connection.*


class ConnectionFragment : Fragment(), DeviceListAdapter.DeviceClickListener {

    var deviceList = ArrayList<BluetoothDevice>()

    private var meterTunnelServer: BluetoothServer? = null


    fun setMeterTunnelServer(server: BluetoothServer) {
        meterTunnelServer = server
    }

    override fun onConnectionToMeterTunnel(device: Int) {
        println("Connection to Meter -> " + deviceList[device].name)
        meterTunnelServer?.connectToServer(deviceList[device])
        Toast.makeText(context, "Connection to Meter -> " + deviceList[device].name, Toast.LENGTH_LONG).show()
    }

    override fun onConnectionToPim(device: Int) {
        println("Connection to PIM -> " + deviceList[device].name)
        Toast.makeText(context, "Connection to PIM - > " + deviceList[device].name, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        deviceList.addAll(bluetoothAdapter.bondedDevices)

        bluetoothPairedDevicesList.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        bluetoothPairedDevicesList.addItemDecoration(SeparatorDecoration(context!!, Color.GRAY, 1.5f))
        val adapter = DeviceListAdapter(deviceList, this)
        bluetoothPairedDevicesList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        deviceList.clear()
    }
}
