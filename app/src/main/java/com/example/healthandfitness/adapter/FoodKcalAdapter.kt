package com.example.healthandfitness.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.model.Food
import java.lang.Exception

class FoodKcalAdapter(private val foodList: ArrayList<Food>, private val context: Context): RecyclerView.Adapter<FoodKcalAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodNameSingleRow)
        val imgFood: ImageView = view.findViewById(R.id.imgFoodSingleRow)
        val imgExtra: ImageView = view.findViewById(R.id.imgExtraFoodSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_food,parent,false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        try{
            holder.apply{
                txtFoodName.text = foodList[position].name
                imgFood.setImageResource(R.drawable.ic_launcher_background)
                imgExtra.setImageResource(R.drawable.ic_remove)
                imgExtra.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("Confirm Remove")
                        .setMessage("Are you sure you want to remove this food from your intake?")
                        .setPositiveButton("Yes") { dialogInterface, i ->

                            if(Constants.kcalFragmentReference!=null){
                                Constants.kcalFragmentReference?.removeFoodFromList(position)
                                dialogInterface.dismiss()
                            }
                            else{
                                Toast.makeText(context,"Failed to remove food",Toast.LENGTH_SHORT).show()
                            }

                        }
                        .setNegativeButton("No") { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
            }
        }
        catch(e: Exception){
            Log.d("Vinesh","Error ${e.message}")
        }

    }

    override fun getItemCount(): Int {
        return foodList.size
    }

}