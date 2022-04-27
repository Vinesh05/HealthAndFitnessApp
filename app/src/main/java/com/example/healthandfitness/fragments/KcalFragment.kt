package com.example.healthandfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.FoodAdapter
import com.example.healthandfitness.databinding.FragmentKcalBinding
import com.example.healthandfitness.model.Food

class KcalFragment : Fragment() {

    lateinit var binding: FragmentKcalBinding
    lateinit var intakeList: ArrayList<Food>
    lateinit var intakeAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKcalBinding.inflate(inflater)

        intakeList = ArrayList()
        intakeList.add(Food("Pizza",""))
        intakeList.add(Food("Burger",""))
        intakeList.add(Food("Pizza1",""))
        intakeList.add(Food("Burger1",""))
        intakeList.add(Food("Pizza2",""))
        intakeList.add(Food("Burger2",""))
        intakeList.add(Food("Pizza3",""))
        intakeList.add(Food("Burger3",""))
        intakeList.add(Food("Pizza4",""))
        intakeList.add(Food("Burger4",""))

        intakeAdapter = FoodAdapter(intakeList,requireContext(),R.drawable.ic_right_arrow)

        binding.apply {

            recyclerViewKcalIntake.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = intakeAdapter
            }

        }

        return binding.root
    }

}