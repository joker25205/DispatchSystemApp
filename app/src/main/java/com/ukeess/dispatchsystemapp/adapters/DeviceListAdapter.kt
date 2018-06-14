package com.ukeess.dispatchsystemapp.adapters

import android.bluetooth.BluetoothDevice
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ukeess.dispatchsystemapp.R

class DeviceListAdapter(val deviceList: ArrayList<BluetoothDevice>, val deviceClickListener: DeviceClickListener) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = deviceList[position].name
        holder.macAddress.text = deviceList[position].address
        holder.connectToPIM.setOnClickListener {
            deviceClickListener.onConnectionToPim(position)
        }
        holder.connectToTunnel.setOnClickListener {
            deviceClickListener.onConnectionToMeterTunnel(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false))

    override fun getItemCount(): Int = deviceList.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val macAddress: TextView = itemView.findViewById(R.id.tv_mac_address)
        val connectToPIM: View = itemView.findViewById(R.id.connect_to_pim_app)
        val connectToTunnel: View = itemView.findViewById(R.id.connect_to_tunnel_app)
    }

    interface DeviceClickListener {

        fun onConnectionToPim(device: Int)

        fun onConnectionToMeterTunnel(device: Int)
    }

}