package com.metro.gwuexplorer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class AlertAdapter constructor(private val alerts: List<Alert>) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.row_alert, parent, false)
        return ViewHolder(itemView)
    }
    override fun getItemCount(): Int = alerts.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAlerts = alerts[position]
        holder.lineNameTextView.text = currentAlerts.stationName
    }
    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        val lineNameTextView: TextView = view.findViewById(R.id.stationName)
    }
}
