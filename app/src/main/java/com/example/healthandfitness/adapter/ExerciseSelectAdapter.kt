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
import com.example.healthandfitness.activities.SelectExerciseActivity
import com.example.healthandfitness.activities.TodaysExercisePlanActivity
import com.example.healthandfitness.model.Exercise
import com.example.healthandfitness.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ExerciseSelectAdapter(private val exerciseList: ArrayList<Exercise>, private val context: Context, private val imgExtraSource: Int): RecyclerView.Adapter<ExerciseSelectAdapter.ExerciseSelectViewHolder>() {

    class ExerciseSelectViewHolder(view: View): RecyclerView.ViewHolder(view){
        val mainLayout: ConstraintLayout = view.findViewById(R.id.mainCardExerciseSingleRow)
        val txtExerciseName: TextView = view.findViewById(R.id.txtExerciseNameSingleRow)
        val imgExercise: ImageView = view.findViewById(R.id.imgExerciseSingleRow)
        val imgExtra: ImageView = view.findViewById(R.id.imgExtraExerciseSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseSelectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_exercise,parent,false)
        return ExerciseSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseSelectViewHolder, position: Int) {
        try{
            holder.apply{
                txtExerciseName.text = exerciseList[position].name
                imgExercise.setImageResource(R.drawable.logo)
                imgExtra.setImageResource(imgExtraSource)
                mainLayout.setOnClickListener {
                    val exercise = HashMap<String,Any>()
                    exercise["name"] = Constants.allExercisesList[position].name
                    exercise["calBurnt"] = Constants.allExercisesList[position].calBurnt
                    exercise["image"] = Constants.allExercisesList[position].image
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!).collection("exercisePlan").document().set(exercise).addOnCompleteListener { addExerciseTask ->
                        if(addExerciseTask.isSuccessful){
                            Intent(context,TodaysExercisePlanActivity::class.java).also{
                                context.startActivity(it)
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
        return exerciseList.size
    }

}