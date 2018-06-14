package com.ukeess.dispatchsystemapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ukeess.dispatchsystemapp.R

class LogsAdapter(val context: Context, val logs: ArrayList<String>) : RecyclerView.Adapter<LogsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsAdapter.ViewHolder =
            LogsAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.logs_item, parent, false))

    override fun onBindViewHolder(holder: LogsAdapter.ViewHolder, position: Int) {
        if (position == 0) {
            holder.title.setTextColor(context.resources.getColor(R.color.colorBlack))
            holder.title.textSize = context.resources.getDimension(R.dimen.textSizeSelectItems)
            holder.title.text = logs[position]
        } else {
            holder.title.setTextColor(context.resources.getColor(R.color.colorItem))
            holder.title.textSize = context.resources.getDimension(R.dimen.textSizeSelectItems)
            holder.title.text = logs[position]
        }
            holder.title.text = logs[position]

    }

    override fun getItemCount(): Int = logs.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.logText)
    }
}