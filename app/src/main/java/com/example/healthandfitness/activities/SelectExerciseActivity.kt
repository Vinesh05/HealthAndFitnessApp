package com.example.healthandfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.ExerciseSelectAdapter
import com.example.healthandfitness.databinding.ActivitySelectExerciseBinding
import com.example.healthandfitness.model.Exercise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SelectExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectExerciseBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var exerciseSelectAdapter: ExerciseSelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        binding.apply{
            recyclerViewSelectExerciseActivity.layoutManager = LinearLayoutManager(this@SelectExerciseActivity)
        }

        getAllExercises()

    }

    private fun getAllExercises(){

        Constants.allExercisesList.clear()
        binding.progressBarSelectExerciseActivity.visibility = View.VISIBLE
        db.collection("exercises").get().addOnCompleteListener { getAllExercisesTask->
            if (getAllExercisesTask.isSuccessful){
                Log.d("Vinesh","get all exercises successful")
                getAllExercisesTask.result.documents.forEach { exercise ->
                    Log.d("Vinesh","get all exercises for each")
                    val name = exercise.data?.get("name").toString()
                    val calBurnt = exercise.data?.get("calBurnt").toString()
                    val image = exercise.data?.get("image").toString()
                    Constants.allExercisesList.add(Exercise(name,calBurnt,image))
                }
            }
            else{
                Toast.makeText(this@SelectExerciseActivity,"Failed to retrieve all exercises",Toast.LENGTH_SHORT).show()
            }
            exerciseSelectAdapter = ExerciseSelectAdapter(Constants.allExercisesList,this@SelectExerciseActivity,R.drawable.ic_right_arrow)
            binding.apply{
                recyclerViewSelectExerciseActivity.adapter = exerciseSelectAdapter
                progressBarSelectExerciseActivity.visibility = View.GONE
            }
        }

    }

}