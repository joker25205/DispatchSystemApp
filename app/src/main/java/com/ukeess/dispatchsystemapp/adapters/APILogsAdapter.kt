package com.ukeess.dispatchsystemapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ukeess.dispatchsystemapp.R
import com.ukeess.dispatchsystemapp.enums.LogType
import com.ukeess.dispatchsystemapp.model.APILogsInfo

class APILogsAdapter(val context: Context, val logInfoList: ArrayList<APILogsInfo>, val itemClickListener: APILogsAdapter.ItemClickListener) : RecyclerView.Adapter<APILogsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): APILogsAdapter.ViewHolder =
            APILogsAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.api_logs_item, parent, false))

    override fun onBindViewHolder(holder: APILogsAdapter.ViewHolder, position: Int) {
        if (position == 0 || position == 1) {
            holder.message.setTextColor(context.resources.getColor(R.color.colorBlack, null))
            holder.currentLogDate.setTextColor(context.resources.getColor(R.color.colorBlack, null))
            holder.contentMessage.setTextColor(context.resources.getColor(R.color.colorBlack, null))
            holder.contentMessage.visibility = View.VISIBLE

            holder.message.text = logInfoList[position].message
            holder.currentLogDate.text = logInfoList[position].timeStamp
            holder.contentMessage.text = logInfoList[position].jsonData
        } else {
            holder.contentMessage.visibility = View.GONE
            holder.message.text = logInfoList[position].message
            holder.currentLogDate.text = logInfoList[position].timeStamp
        }

        if (logInfoList[position].typeMessage == LogType.RESPONSE)
            holder.messageTypeImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_received_message, null))
        else
            holder.messageTypeImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_send_message, null))


        holder.itemSelect.setOnClickListener {
            if (position == 0 || position == 1)
                return@setOnClickListener
            else
                itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = logInfoList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.logText)
        val messageTypeImage: ImageView = itemView.findViewById(R.id.statusMessage)
        val currentLogDate: TextView = itemView.findViewById(R.id.dateText)
        val contentMessage: TextView = itemView.findViewById(R.id.contentMessage)
        val itemSelect: LinearLayout = itemView.findViewById(R.id.itemSelect)
    }

    interface ItemClickListener {

        fun onItemClick(itemPosition: Int)
    }
}