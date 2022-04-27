package com.example.healthandfitness.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WaterHistoryAdapter(private val timeList: ArrayList<Long>,private val context: Context): RecyclerView.Adapter<WaterHistoryAdapter.WaterHistoryViewHolder>() {

    class WaterHistoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtWaterTime: TextView = view.findViewById(R.id.txtWaterTimeSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_water_history,parent,false)
        return WaterHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WaterHistoryViewHolder, position: Int) {
        try{
            val date = Date(timeList[position])
            val formatter: DateFormat = SimpleDateFormat("HH:mm")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            holder.txtWaterTime.text = formatter.format(date)
        }
        catch(e: Exception){
            Log.d("Vinesh","Error: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

}