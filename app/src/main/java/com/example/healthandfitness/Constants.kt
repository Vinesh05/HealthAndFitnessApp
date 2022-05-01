package com.example.healthandfitness

import com.example.healthandfitness.fragments.DietPlanFragment
import com.example.healthandfitness.fragments.KcalFragment
import com.example.healthandfitness.model.DietPlan
import com.example.healthandfitness.model.Food

object Constants {

    val allAvailableFoodsList = ArrayList<Food>()
    val usersCalorieIntakeList = ArrayList<Food>()
    var userCalorieGoal = 0
    var kcalFragmentReference: KcalFragment? = null
//    var dietPlanFragmentReference: DietPlanFragment? = null

    var morningDietPlanFoodList = ArrayList<Food>()
    var afternoonDietPlanFoodList = ArrayList<Food>()
    var eveningDietPlanFoodList = ArrayList<Food>()

    var dietPlanList = ArrayList<DietPlan>()

}