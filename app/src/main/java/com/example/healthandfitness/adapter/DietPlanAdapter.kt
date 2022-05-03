package com.example.healthandfitness.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.R
import com.example.healthandfitness.activities.SelectFoodFromListActivity
import com.example.healthandfitness.model.DietPlan

class DietPlanAdapter(private val dietList: ArrayList<DietPlan>, private val context: Context): RecyclerView.Adapter<DietPlanAdapter.DietPlanViewHolder>() {

    val adapterList = ArrayList<FoodDietPlanAdapter>()

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
                    Intent(context,SelectFoodFromListActivity::class.java).also{
                        it.putExtra("fragment","dietPlan")
                        it.putExtra("partOfDay", (dietList[position].partOfDay))
                        context.startActivity(it)
                    }
                }
                recyclerViewDietPlan.layoutManager = LinearLayoutManager(context)
                val adapter = FoodDietPlanAdapter(dietList[position].foodList,context,dietList[position].partOfDay)
                if(adapterList.size<3){
                    adapterList.add(adapter)
                }
                else{
                    adapterList.removeAt(position)
                    adapterList.add(position,adapter)
                }

                recyclerViewDietPlan.adapter = adapter
            }
        }
        catch(e: Exception){
            Log.d("Vinesh","Error: ${e.message}")
        }
    }

    fun notifyInnerDataChanged(position: Int){
        adapterList[position].notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dietList.size
    }

}