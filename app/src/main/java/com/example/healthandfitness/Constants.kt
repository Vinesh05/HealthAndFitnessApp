package com.example.healthandfitness

import com.example.healthandfitness.fragments.DietPlanFragment
import com.example.healthandfitness.fragments.KcalFragment
import com.example.healthandfitness.model.DietPlan
import com.example.healthandfitness.model.Exercise
import com.example.healthandfitness.model.Food

object Constants {

    val allAvailableFoodsList = ArrayList<Food>()
    val usersCalorieIntakeList = ArrayList<Food>()
    var userCalorieGoal = 0
    var kcalFragmentReference: KcalFragment? = null

    var morningDietPlanFoodList = ArrayList<Food>()
    var afternoonDietPlanFoodList = ArrayList<Food>()
    var eveningDietPlanFoodList = ArrayList<Food>()

    var dietPlanList = ArrayList<DietPlan>()

    var todaysProtein = 0.0
    var todaysCarbs = 0.0
    var todaysFats = 0.0
    var todaysFiber = 0.0

    var allExercisesList = ArrayList<Exercise>()
    var exercisePlanList = ArrayList<Exercise>()

}