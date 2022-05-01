package com.example.healthandfitness.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.databinding.ActivityMainBinding
import com.example.healthandfitness.fragments.*
import com.example.healthandfitness.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val kcalFragment = KcalFragment()
    private val dietPlanFragment = DietPlanFragment()
    private val exerciseFragment = ExerciseFragment()
    private val blogsFragment = BlogsFragment()
    private val waterFragment = WaterReminderFragment()
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser==null){
            Intent(this,LoginActivity::class.java).also{
                startActivity(it)
            }
        }

        db.collection("foods").get().addOnCompleteListener {
            try{
                if(it.isSuccessful){
                    if(Constants.allAvailableFoodsList.isEmpty()){
                        it.result.documents.forEach {  doc ->
                            val docId = doc.id
                            val name = doc["name"].toString()
                            val carbs = doc["carbs"].toString().toDouble()
                            val fats = doc["fats"].toString().toDouble()
                            val fiber = doc["fiber"].toString().toDouble()
                            val protein = doc["protein"].toString().toDouble()
                            val calories = doc["calories"].toString().toInt()
                            val image = doc["image"].toString()
                            Constants.allAvailableFoodsList.add(Food(docId, name,calories, carbs, fats, fiber, protein, image))
                        }
                    }
                }
                else{
                    Toast.makeText(this@MainActivity, "Failed to retrieve all foods",Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
            }
            catch(e: Exception){
                Toast.makeText(this@MainActivity, "Failed to retrieve all foods",Toast.LENGTH_SHORT).show()
            }
        }

        setUpBottomNavigation()

    }

    private fun setUpBottomNavigation(){

        binding.apply{
            kcalBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, kcalFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_kcal_selected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            dietPlanBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, dietPlanFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_diet_plan_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            exerciseBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, exerciseFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_exercise_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            blogsBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, blogsFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_blogs_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            waterReminderBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, waterFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_water_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                }
            }
            kcalBottomNavigation.callOnClick()
        }

    }

    override fun onBackPressed() {
        finishAffinity()
    }

}