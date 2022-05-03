package com.example.healthandfitness.adapter

import android.content.Context
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

class FoodSelectDietPlanAdapter(private val foodList: ArrayList<Food>, private val context: Context, private val imgExtraSource: Int, private val partOfDay: String): RecyclerView.Adapter<FoodSelectDietPlanAdapter.FoodSelectDietPlanViewHolder>() {

    class FoodSelectDietPlanViewHolder(view: View): RecyclerView.ViewHolder(view){
        val mainLayout: ConstraintLayout = view.findViewById(R.id.mainCardFoodSingleRow)
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodNameSingleRow)
        val imgFood: ImageView = view.findViewById(R.id.imgFoodSingleRow)
        val imgExtra: ImageView = view.findViewById(R.id.imgExtraFoodSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodSelectDietPlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_food,parent,false)
        return FoodSelectDietPlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodSelectDietPlanViewHolder, position: Int) {
        try{
            holder.apply{
                txtFoodName.text = foodList[position].name
                imgFood.setImageResource(R.drawable.logo)
                imgExtra.setImageResource(imgExtraSource)
                mainLayout.setOnClickListener {
                    val todayDate = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val todaysDateString = formatter.format(todayDate)
                    val food = HashMap<String,Any>()
                    food["name"] = Constants.allAvailableFoodsList[position].name
                    food["calories"] = Constants.allAvailableFoodsList[position].calories
                    food["carbs"] = Constants.allAvailableFoodsList[position].carbs
                    food["protein"] = Constants.allAvailableFoodsList[position].protein
                    food["fats"] = Constants.allAvailableFoodsList[position].fats
                    food["fiber"] = Constants.allAvailableFoodsList[position].fiber
                    food["image"] = Constants.allAvailableFoodsList[position].image
                    food["date"] = todaysDateString
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!).collection("dietPlan").get(Source.SERVER).addOnCompleteListener { getAllDocsTask ->
                        Log.d("Vinesh","getAllDocsTask")
                        if(getAllDocsTask.isSuccessful){
                            getAllDocsTask.result.documents.forEach { doc->
                                Log.d("Vinesh","getAllDocsTask..$partOfDay")
                                if(doc.data?.get("partOfDay").toString()==partOfDay){
                                    doc.reference.collection("foodList").add(food).addOnCompleteListener { addFoodTask->
                                        Log.d("Vinesh","addFoodTask")
                                        if(addFoodTask.isSuccessful){
                                            Intent(context,MainActivity::class.java).also{
                                                context.startActivity(it)
                                            }
                                        }
                                    }
                                    Intent(context,MainActivity::class.java).also{
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