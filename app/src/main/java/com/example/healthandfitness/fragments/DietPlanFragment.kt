package com.example.healthandfitness.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.DietPlanAdapter
import com.example.healthandfitness.databinding.FragmentDietPlanBinding
import com.example.healthandfitness.model.DietPlan
import com.example.healthandfitness.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DietPlanFragment : Fragment() {

    private lateinit var binding: FragmentDietPlanBinding
    private lateinit var dietPlanAdapter: DietPlanAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDietPlanBinding.inflate(inflater)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        getDietPlan()

        Constants.dietPlanList.clear()
        Constants.dietPlanList.add(DietPlan("Morning",Constants.morningDietPlanFoodList))
        Constants.dietPlanList.add(DietPlan("Afternoon",Constants.afternoonDietPlanFoodList))
        Constants.dietPlanList.add(DietPlan("Evening",Constants.eveningDietPlanFoodList))

        binding.apply{
            recyclerViewDietPlan.layoutManager = LinearLayoutManager(requireContext())
            carbsProgressBar.max = Constants.userCalorieGoal*0.45.toInt()
            proteinProgressBar.max = Constants.userCalorieGoal*0.2.toInt()
            fatsProgressBar.max = Constants.userCalorieGoal*0.2.toInt()
            fiberProgressBar.max = Constants.userCalorieGoal*0.15.toInt()
        }

        return binding.root
    }

    private fun getDietPlan(){
        binding.progressBarDietPlanFragment.visibility = View.VISIBLE

        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("dietPlan").get(Source.SERVER).addOnCompleteListener { getAllDietPlansTask ->
            if(getAllDietPlansTask.isSuccessful){
                Log.d("Vinesh","Successful")
                getAllDietPlansTask.result.documents.forEach { doc->
                    Log.d("Vinesh","Doc id: ${doc.id}")
                    when(doc.data?.get("partOfDay").toString()){
                        "morning"->{
                            doc.reference.collection("foodList").get().addOnCompleteListener { getMorningDietTask ->
                                if(getMorningDietTask.isSuccessful){
                                    Constants.morningDietPlanFoodList.clear()
                                    getMorningDietTask.result.documents.forEach { morningFood ->

                                        val todayDate = Calendar.getInstance().time
                                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                                        val todaysDateString = formatter.format(todayDate)
                                        val date = morningFood.data?.get("date").toString()
                                        if(date==todaysDateString){
                                            val docId = morningFood.id
                                            val name = morningFood.data?.get("name").toString()
                                            val calories = morningFood.data?.get("calories").toString().toInt()
                                            val carbs = morningFood.data?.get("carbs").toString().toDouble()
                                            val protein = morningFood.data?.get("protein").toString().toDouble()
                                            val fats = morningFood.data?.get("fats").toString().toDouble()
                                            val fiber = morningFood.data?.get("fiber").toString().toDouble()
                                            val image = morningFood.data?.get("image").toString()

                                            val food = Food(docId, name, calories, carbs, fats, fiber, protein, image)
                                            Constants.morningDietPlanFoodList.add(food)
                                            Log.d("Vinesh","Morning: ${food.name}")
                                        }
                                        else{
                                            deleteAllDietPlanFoodLists()
                                            return@forEach
                                        }
                                        Constants.dietPlanList.removeAt(0)
                                        Constants.dietPlanList.add(0,DietPlan("morning",Constants.morningDietPlanFoodList))
                                        dietPlanAdapter.notifyInnerDataChanged(0)
                                        binding.progressBarDietPlanFragment.visibility = View.GONE
                                    }
                                }
                                else{
                                    binding.progressBarDietPlanFragment.visibility = View.GONE
                                    Toast.makeText(requireContext(),"Some Error Occurred While Retrieving Morning Diet Plans",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        "afternoon"->{
                            doc.reference.collection("foodList").get().addOnCompleteListener { getMorningDietTask ->
                                if(getMorningDietTask.isSuccessful){
                                    Constants.afternoonDietPlanFoodList.clear()
                                    getMorningDietTask.result.documents.forEach { morningFood ->
                                        val todayDate = Calendar.getInstance().time
                                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                                        val todaysDateString = formatter.format(todayDate)
                                        val date = morningFood.data?.get("date").toString()
                                        if(date==todaysDateString){
                                            val docId = morningFood.id
                                            val name = morningFood.data?.get("name").toString()
                                            val calories = morningFood.data?.get("calories").toString().toInt()
                                            val carbs = morningFood.data?.get("carbs").toString().toDouble()
                                            val protein = morningFood.data?.get("protein").toString().toDouble()
                                            val fats = morningFood.data?.get("fats").toString().toDouble()
                                            val fiber = morningFood.data?.get("fiber").toString().toDouble()
                                            val image = morningFood.data?.get("image").toString()

                                            val food = Food(docId, name, calories, carbs, fats, fiber, protein, image)
                                            Constants.afternoonDietPlanFoodList.add(food)
                                            Log.d("Vinesh","Afternoon: ${food.name}")
                                        }
                                        else{
                                            deleteAllDietPlanFoodLists()
                                            return@forEach
                                        }
                                        Constants.dietPlanList.removeAt(1)
                                        Constants.dietPlanList.add(1,DietPlan("afternoon",Constants.afternoonDietPlanFoodList))
                                        dietPlanAdapter.notifyInnerDataChanged(1)
                                        binding.progressBarDietPlanFragment.visibility = View.GONE
                                    }
                                }
                                else{
                                    binding.progressBarDietPlanFragment.visibility = View.GONE
                                    Toast.makeText(requireContext(),"Some Error Occurred While Retrieving Afternoon Diet Plans",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        "evening"->{
                            doc.reference.collection("foodList").get().addOnCompleteListener { getMorningDietTask ->
                                if(getMorningDietTask.isSuccessful){
                                    Constants.eveningDietPlanFoodList.clear()
                                    getMorningDietTask.result.documents.forEach { morningFood ->
                                        val todayDate = Calendar.getInstance().time
                                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                                        val todaysDateString = formatter.format(todayDate)
                                        val date = morningFood.data?.get("date").toString()
                                        if(date==todaysDateString){
                                            val docId = morningFood.id
                                            val name = morningFood.data?.get("name").toString()
                                            val calories = morningFood.data?.get("calories").toString().toInt()
                                            val carbs = morningFood.data?.get("carbs").toString().toDouble()
                                            val protein = morningFood.data?.get("protein").toString().toDouble()
                                            val fats = morningFood.data?.get("fats").toString().toDouble()
                                            val fiber = morningFood.data?.get("fiber").toString().toDouble()
                                            val image = morningFood.data?.get("image").toString()

                                            val food = Food(docId, name, calories, carbs, fats, fiber, protein, image)
                                            Constants.eveningDietPlanFoodList.add(food)
                                            Log.d("Vinesh","Evening: ${food.name}")
                                        }
                                        else{
                                            deleteAllDietPlanFoodLists()
                                            return@forEach
                                        }
                                        Constants.dietPlanList.removeAt(2)
                                        Constants.dietPlanList.add(2,DietPlan("evening",Constants.eveningDietPlanFoodList))
                                        dietPlanAdapter.notifyInnerDataChanged(2)
                                        binding.progressBarDietPlanFragment.visibility = View.GONE
                                    }
                                }
                                else{
                                    binding.progressBarDietPlanFragment.visibility = View.GONE
                                    Toast.makeText(requireContext(),"Some Error Occurred While Retrieving Evening Diet Plans",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else->{
                            binding.progressBarDietPlanFragment.visibility = View.GONE
                            Toast.makeText(requireContext(),"Some Error Occurred While Retrieving Diet Plans",Toast.LENGTH_SHORT).show()
                        }
                    }
                    dietPlanAdapter = DietPlanAdapter(Constants.dietPlanList,requireContext())
                    binding.recyclerViewDietPlan.adapter = dietPlanAdapter
                }
            }
            else{
                binding.progressBarDietPlanFragment.visibility = View.GONE
                Log.d("Vinesh","Diet Plans Exception: ${getAllDietPlansTask.exception?.message}")
                Toast.makeText(requireContext(),"Some Error Occurred While Retrieving Diet Plans",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun deleteAllDietPlanFoodLists() {
        binding.progressBarDietPlanFragment.visibility = View.GONE
    }

}