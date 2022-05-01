package com.example.healthandfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.FoodSelectDietPlanAdapter
import com.example.healthandfitness.adapter.FoodSelectKcalAdapter
import com.example.healthandfitness.databinding.ActivitySelectFoodFromListBinding

class SelectFoodFromListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectFoodFromListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectFoodFromListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapterType = intent.getStringExtra("fragment").toString()

        binding.apply{
            recyclerViewSelectFoodActivity.apply{
                layoutManager = LinearLayoutManager(this@SelectFoodFromListActivity)
                adapter = if(adapterType=="kcal"){
                    FoodSelectKcalAdapter(Constants.allAvailableFoodsList,this@SelectFoodFromListActivity,R.drawable.ic_right_arrow)
                }
                else{
                    val partOfDay = intent.getStringExtra("partOfDay").toString()
                    FoodSelectDietPlanAdapter(Constants.allAvailableFoodsList,this@SelectFoodFromListActivity,R.drawable.ic_right_arrow,partOfDay)
                }
            }
        }

    }
}