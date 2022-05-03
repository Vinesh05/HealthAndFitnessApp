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
import com.example.healthandfitness.activities.TodaysExercisePlanActivity
import com.example.healthandfitness.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class ExercisePlanAdapter(private val exerciseList: ArrayList<Exercise>, private val context: Context, private val imgExtraSource: Int): RecyclerView.Adapter<ExercisePlanAdapter.ExercisePlanViewHolder>() {

    class ExercisePlanViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtExerciseName: TextView = view.findViewById(R.id.txtExerciseNameSingleRow)
        val imgExercise: ImageView = view.findViewById(R.id.imgExerciseSingleRow)
        val imgExtra: ImageView = view.findViewById(R.id.imgExtraExerciseSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisePlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_exercise,parent,false)
        return ExercisePlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExercisePlanViewHolder, position: Int) {
        try{
            holder.apply{
                txtExerciseName.text = exerciseList[position].name
                imgExercise.setImageResource(R.drawable.logo)
                imgExtra.setImageResource(imgExtraSource)
                imgExtra.setOnClickListener {
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!).collection("exercisePlan").get().addOnCompleteListener { getAllExercisesPlanned ->
                        if(getAllExercisesPlanned.isSuccessful){
                            var deleted = false
                            getAllExercisesPlanned.result.documents.forEach { exercise ->
                                val name = exercise.data?.get("name")
                                if((name==exerciseList[position].name) and (!deleted)){
                                    Log.d("Vinesh","Deleting: $name")
                                    exercise.reference.delete()
                                    deleted = true
                                    Constants.exercisePlanList.removeAt(position)
                                }
                            }
                            notifyDataSetChanged()
                        }
                        else{
                            Toast.makeText(context,"Failed to Remove Exercise",Toast.LENGTH_SHORT).show()
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