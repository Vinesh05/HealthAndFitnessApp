package com.example.healthandfitness.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.R
import com.example.healthandfitness.model.DietPlan

class DietPlanAdapter(private val dietList: ArrayList<DietPlan>, private val context: Context): RecyclerView.Adapter<DietPlanAdapter.DietPlanViewHolder>() {

    class DietPlanViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtPartOfDay: TextView = view.findViewById(R.id.txtPartOfDayDietPlanSingleRow)
        val btnAddDietPlan: AppCompatImageButton = view.findViewById(R.id.btnAddDietPlanSingleRow)
        val recyclerViewDietPlan: RecyclerView = view.findViewById(R.id.recyclerViewDietPlanFoodSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietPlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_diet_plan,parent,false)
        return DietPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietPlanViewHolder, position: Int) {
        try{
            holder.apply{
                txtPartOfDay.text = "${dietList[position].partOfDay}:"
                btnAddDietPlan.setOnClickListener {
                    Toast.makeText(context,"Button Add Food Clicked",Toast.LENGTH_SHORT).show()
                }
                recyclerViewDietPlan.layoutManager = LinearLayoutManager(context)
                recyclerViewDietPlan.adapter = FoodAdapter(dietList[position].foodList,context,R.drawable.ic_remove)
            }
        }
        catch(e: Exception){
            Log.d("Vinesh","Error: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return dietList.size
    }

}