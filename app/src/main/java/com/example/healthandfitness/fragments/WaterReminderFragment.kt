package com.example.healthandfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.WaterHistoryAdapter
import com.example.healthandfitness.databinding.FragmentWaterReminderBinding

class WaterReminderFragment : Fragment() {

    private lateinit var binding: FragmentWaterReminderBinding
    private lateinit var waterHistoryList: ArrayList<Long>
    private lateinit var waterHistoryAdapter: WaterHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaterReminderBinding.inflate(inflater)

        waterHistoryList = ArrayList()
        waterHistoryList.add(System.currentTimeMillis())
        waterHistoryList.add(System.currentTimeMillis())
        waterHistoryList.add(System.currentTimeMillis())
        waterHistoryList.add(System.currentTimeMillis())

        waterHistoryAdapter = WaterHistoryAdapter(waterHistoryList,requireContext())

        binding.apply{
            recyclerViewWaterHistory.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = waterHistoryAdapter
            }
        }

        return binding.root
    }

}