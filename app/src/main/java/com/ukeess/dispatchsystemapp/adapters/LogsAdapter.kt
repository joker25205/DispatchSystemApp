package com.ukeess.dispatchsystemapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ukeess.dispatchsystemapp.R
import com.ukeess.dispatchsystemapp.enums.LogType
import com.ukeess.dispatchsystemapp.model.LogInfo

class LogsAdapter(val context: Context, val logInfoList: ArrayList<LogInfo>) : RecyclerView.Adapter<LogsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsAdapter.ViewHolder =
            LogsAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tunnel_logs_item, parent, false))

    override fun onBindViewHolder(holder: LogsAdapter.ViewHolder, position: Int) {
        if (position == 0 || position == 1) {
            holder.message.setTextColor(context.resources.getColor(R.color.colorBlack))
            holder.currentLogDate.setTextColor(context.resources.getColor(R.color.colorBlack))

            holder.message.text = logInfoList[position].message
            holder.currentLogDate.text = logInfoList[position].timeStamp
        } else {
            holder.message.text = logInfoList[position].message
            holder.currentLogDate.text = logInfoList[position].timeStamp
        }

        if (logInfoList[position].typeMessage == LogType.RESPONSE)
            holder.messageTypeImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_received_message, null))
        else
            holder.messageTypeImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_send_message, null))
    }

    override fun getItemCount(): Int = logInfoList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.logText)
        val messageTypeImage: ImageView = itemView.findViewById(R.id.statusMessage)
        val currentLogDate: TextView = itemView.findViewById(R.id.dateText)
    }
}