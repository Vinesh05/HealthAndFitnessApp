package com.example.healthandfitness.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.R
import com.example.healthandfitness.model.Food

class FoodAdapter(private val foodList: ArrayList<Food>, private val context: Context): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodNameSingleRow)
        val imgFood: ImageView = view.findViewById(R.id.imgFoodSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_food,parent,false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.txtFoodName.text = foodList[position].name
        holder.imgFood.setImageResource(R.drawable.ic_launcher_background)
        Log.d("Vinesh", foodList[position].name)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

}