package com.example.healthandfitness.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.ExerciseHistoryAdapter
import com.example.healthandfitness.adapter.ExercisePlanAdapter
import com.example.healthandfitness.container.DayViewContainer
import com.example.healthandfitness.databinding.ActivityTodaysExercisePlanBinding
import com.example.healthandfitness.model.Exercise
import com.example.healthandfitness.model.ExerciseHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.*

class TodaysExercisePlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodaysExercisePlanBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var exercisePlanAdapter: ExercisePlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodaysExercisePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        binding.apply{
            btnAddExercisePlanActivity.setOnClickListener {
                Intent(this@TodaysExercisePlanActivity,SelectExerciseActivity::class.java).also {
                    startActivity(it)
                }
            }
            btnDoneExercisePlan.setOnClickListener {
                val exerciseHistory = HashMap<String,Any>()
                var totalCalBurnt = 0
                Constants.exercisePlanList.forEach {
                    totalCalBurnt += it.calBurnt.toInt()
                }
                val time = System.currentTimeMillis()
                exerciseHistory["calBurnt"] = totalCalBurnt
                exerciseHistory["time"] = time
                db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("exerciseHistory").add(exerciseHistory).addOnCompleteListener {
                    Intent(this@TodaysExercisePlanActivity,MainActivity::class.java).also{
                        startActivity(it)
                    }
                }
            }
            recyclerViewExercisePlan.layoutManager = LinearLayoutManager(this@TodaysExercisePlanActivity)
        }

        getExercisePlan()

    }

    private fun getExercisePlan(){
        Constants.exercisePlanList.clear()
        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("exercisePlan").get().addOnCompleteListener { getAllExPlan->
            if(getAllExPlan.isSuccessful){
                getAllExPlan.result.documents.forEach { docs->
                    val name = docs.data?.get("name").toString()
                    val calBurnt = docs.data?.get("calBurnt").toString()
                    val image = docs.data?.get("image").toString()
                    Constants.exercisePlanList .add(Exercise(name,calBurnt,image))
                }
                exercisePlanAdapter = ExercisePlanAdapter(Constants.exercisePlanList,this,R.drawable.ic_remove)
                binding.recyclerViewExercisePlan.adapter = exercisePlanAdapter
            }
            else{
                Toast.makeText(this,"Check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        Intent(this,MainActivity::class.java).also{
            startActivity(it)
        }
    }
}