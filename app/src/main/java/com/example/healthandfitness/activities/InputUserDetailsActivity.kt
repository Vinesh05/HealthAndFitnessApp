package com.example.healthandfitness.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.healthandfitness.R
import com.example.healthandfitness.databinding.ActivityInputUserDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class InputUserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputUserDetailsBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "HealthAndFitnessSharedPreferences",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        binding.apply{
            spinnerActivityUserInputActivity.adapter = ArrayAdapter(this@InputUserDetailsActivity,R.layout.drop_down_item,resources.getStringArray(R.array.activity_levels))
            spinnerGenderUserInputActivity.adapter = ArrayAdapter(this@InputUserDetailsActivity,R.layout.drop_down_item,resources.getStringArray(R.array.genders))
            spinnerWeightGoalUserInputActivity.adapter = ArrayAdapter(this@InputUserDetailsActivity,R.layout.drop_down_item,resources.getStringArray(R.array.weight_goals))
            btnSubmitUserInputActivity.setOnClickListener {

                progressBarUserInputActivity.visibility = View.VISIBLE

                val age = edtTxtAgeUserInputActivity.text.toString()
                val weight = edtTxtWeightUserInputActivity.text.toString()
                val height = edtTxtHeightUserInputActivity.text.toString()
                val gender = spinnerGenderUserInputActivity.selectedItem as String
                val activity = spinnerActivityUserInputActivity.selectedItem as String
                val weightGoal = spinnerWeightGoalUserInputActivity.selectedItem as String

                if(age.isNotBlank() and weight.isNotBlank()){
                    try{
                        val ageNumber = Integer.parseInt(age)
                        val weightNumber = Integer.parseInt(weight)
                        val heightNumber = Integer.parseInt(height)

                        val bmr = if(gender=="Male"){
                            66 + (13.7*weightNumber) + (5*heightNumber) -(6.8*ageNumber)
                        }
                        else{
                            655 + (9.6*weightNumber) + (1.8*heightNumber) - (4.7*ageNumber)
                        }

                        val calorieIntake = when(activity){
                            "Sedentary (Little or no exercise)"->bmr*1.2
                            "Light (Exercise 1–3 times/week)"->bmr*1.375
                            "Moderate (Exercise 4–5 times/week)"->bmr*1.55
                            "Very Active (Intense Exercise Every Day)"->bmr*1.725
                            "Extra Active (Physical Job)"->bmr*1.9
                            else->0.0
                        }

                        val finalCalorieIntake = when(weightGoal){
                            "Mild Weight Loss (0.25 kg/week)" -> calorieIntake*0.88
                            "Weight Loss(0.5 kg/week)" -> calorieIntake*0.76
                            "Maintain Weight" -> calorieIntake
                            "Weight Gain" -> calorieIntake*1.12
                            else -> calorieIntake
                        }

                        sharedPreferences.edit().putInt("calorie-intake",finalCalorieIntake.toInt()).apply()

                        val userData = HashMap<String, Any>()
                        userData["age"] = ageNumber
                        userData["weight"] = weightNumber
                        userData["height"] = heightNumber
                        userData["calorieIntake"] = finalCalorieIntake.toInt()
                        db.collection("users").document(firebaseAuth.currentUser?.uid!!).update(userData).addOnCompleteListener {
                            if(it.isSuccessful){
                                Intent(this@InputUserDetailsActivity,MainActivity::class.java).also{ i ->
                                    startActivity(i)
                                }
                            }
                            else{
                                Toast.makeText(this@InputUserDetailsActivity,"Check Your Internet Connection",Toast.LENGTH_SHORT).show()
                            }
                            progressBarUserInputActivity.visibility = View.VISIBLE
                        }

                    }
                    catch(e: Exception){
                        Toast.makeText(this@InputUserDetailsActivity,"Invalid fields",Toast.LENGTH_SHORT).show()
                    }

                }
                else{
                    Toast.makeText(this@InputUserDetailsActivity,"Please fill all fields",Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
}