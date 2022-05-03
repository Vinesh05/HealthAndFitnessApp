package com.example.healthandfitness.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.R
import com.example.healthandfitness.model.ExerciseHistory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExerciseHistoryAdapter(private val exerciseHistoryList: ArrayList<ExerciseHistory>,private val context: Context): RecyclerView.Adapter<ExerciseHistoryAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtDate: TextView = view.findViewById(R.id.txtDateExerciseHistorySingleRow)
        val txtTime: TextView = view.findViewById(R.id.txtTimeExerciseHistorySingleRow)
        val txtCalBurnt: TextView = view.findViewById(R.id.txtCalBurntExerciseHistorySingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_exercise_history,parent,false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        try{
            val date = getDateOrTime(exerciseHistoryList[position].dateAndTime,"dd/MM/yyyy")
            val time = getDateOrTime(exerciseHistoryList[position].dateAndTime,"hh:mm a")

            holder.apply{
                txtDate.text = date
                txtTime.text = time
                Log.d("Vinesh","date:  $date $time")
                txtCalBurnt.text = "${exerciseHistoryList[position].caloriesBurnt} kcal"
            }
        }
        catch (e: Exception){
            Log.d("Vinesh","Error: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return exerciseHistoryList.size
    }

    private fun getDateOrTime(milliSeconds: Long, dateFormat: String): String{
        val formatter = SimpleDateFormat(dateFormat)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }


}