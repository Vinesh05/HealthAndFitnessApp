package com.example.healthandfitness.model

data class Food(
    val docId: String,
    val name: String,
    val calories: Int,
    val carbs: Double,
    val fats: Double,
    val fiber: Double,
    val protein: Double,
    val image: String
)