package com.example.healthandfitness.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.activities.MainActivity
import com.example.healthandfitness.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FoodDietPlanAdapter(private val foodList: ArrayList<Food>, private val context: Context, private val partOfDay: String): RecyclerView.Adapter<FoodDietPlanAdapter.FoodDietPlanViewHolder>() {

    class FoodDietPlanViewHolder(view: View): RecyclerView.ViewHolder(view){
        val mainLayout: ConstraintLayout = view.findViewById(R.id.mainCardFoodSingleRow)
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodNameSingleRow)
        val imgFood: ImageView = view.findViewById(R.id.imgFoodSingleRow)
        val imgExtra: ImageView = view.findViewById(R.id.imgExtraFoodSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodDietPlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_food,parent,false)
        return FoodDietPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodDietPlanViewHolder, position: Int) {
        try{
            holder.apply{
                txtFoodName.text = foodList[position].name
                imgFood.setImageResource(R.drawable.logo)
                imgExtra.setImageResource(R.drawable.ic_remove)
                imgExtra.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("Confirm Remove")
                        .setMessage("Are you sure you want to remove this food from your plan?")
                        .setPositiveButton("Yes") { dialogInterface, i ->

                            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!).collection("dietPlan").get(Source.SERVER).addOnCompleteListener { getAllDocsTask ->
                                Log.d("Vinesh","getAllDocsTask")
                                if(getAllDocsTask.isSuccessful){
                                    getAllDocsTask.result.documents.forEach { doc->
                                        Log.d("Vinesh","getAllDocsTask..$partOfDay")
                                        if(doc.data?.get("partOfDay").toString()==partOfDay){
                                            doc.reference.collection("foodList").get(Source.SERVER).addOnCompleteListener { getFoodListTask->
                                                Log.d("Vinesh","getFoodListTask")
                                                if(getFoodListTask.isSuccessful){
                                                    getFoodListTask.result.documents.forEach { docFood->
                                                        if(docFood.id == foodList[position].docId){
                                                            docFood.reference.delete().addOnCompleteListener {
                                                                dialogInterface.dismiss()
                                                            }
                                                            return@forEach
                                                        }
                                                    }

                                                }
                                            }
                                            Intent(context, MainActivity::class.java).also{
                                                context.startActivity(it)
                                            }
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(context,"Please Check your Internet connection", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                        .setNegativeButton("No") { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }
                        .show()
                }
                mainLayout.setOnClickListener {

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