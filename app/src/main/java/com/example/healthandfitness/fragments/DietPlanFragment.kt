package com.example.healthandfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.DietPlanAdapter
import com.example.healthandfitness.databinding.FragmentDietPlanBinding
import com.example.healthandfitness.model.DietPlan
import com.example.healthandfitness.model.Food

class DietPlanFragment : Fragment() {

    private lateinit var binding: FragmentDietPlanBinding
    private lateinit var dietPlanList: ArrayList<DietPlan>
    private lateinit var dietPlanAdapter: DietPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDietPlanBinding.inflate(inflater)

        dietPlanList = ArrayList()
        val foodList = ArrayList<Food>()
        foodList.add(Food("Pizza",""))
        foodList.add(Food("Burger",""))

        dietPlanList.add(DietPlan("Morning",foodList))
        dietPlanList.add(DietPlan("Evening",foodList))

        binding.apply{
            recyclerViewDietPlan.layoutManager = LinearLayoutManager(requireContext())
            dietPlanAdapter = DietPlanAdapter(dietPlanList,requireContext())
            recyclerViewDietPlan.adapter = dietPlanAdapter
        }

        return binding.root
    }

}