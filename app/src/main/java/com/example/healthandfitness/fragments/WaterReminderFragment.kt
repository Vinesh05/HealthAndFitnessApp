package com.example.healthandfitness.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.WaterHistoryAdapter
import com.example.healthandfitness.databinding.FragmentWaterReminderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class WaterReminderFragment : Fragment() {

    private lateinit var binding: FragmentWaterReminderBinding
    private lateinit var waterHistoryList: ArrayList<Long>
    private lateinit var waterHistoryAdapter: WaterHistoryAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var todaysWaterIntake = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaterReminderBinding.inflate(inflater)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        waterHistoryList = ArrayList()
        getWaterIntakes()

        binding.apply{
            recyclerViewWaterHistory.apply{
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnAddWaterCounter.setOnClickListener {
                addWaterIntake(System.currentTimeMillis())
            }
        }

        return binding.root
    }

    private fun getWaterIntakes(){
        val currentMilliseconds = System.currentTimeMillis()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var currentDateString = formatter.format(currentMilliseconds)
        currentDateString +=" 00:00:00"

        val dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val localDate= LocalDateTime.parse(currentDateString, dateTimeFormatter)
        val todaysTime= localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()-19800000

        binding.waterFragmentProgressBar.visibility = View.VISIBLE
        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("waterIntake").get(Source.SERVER).addOnCompleteListener { getAllBlogsTask ->
            if(getAllBlogsTask.isSuccessful){
                waterHistoryList.clear()
                todaysWaterIntake = 0
                getAllBlogsTask.result.documents.forEach { blogs->
                    val time = blogs.data?.get("time").toString().toLong()
                    if(time<todaysTime){
                        waterHistoryList.clear()
                        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("waterIntake").get(Source.SERVER).addOnCompleteListener { getAllBlogsTaskDelete ->
                            if(getAllBlogsTaskDelete.isSuccessful){
                                getAllBlogsTaskDelete.result.documents.forEach { docToDelete ->
                                    docToDelete.reference.delete()
                                }
                            }
                            else{
                                getWaterIntakes()
                            }
                        }
                        return@forEach
                    }
                    todaysWaterIntake++
                    waterHistoryList.add(time)
                }
                waterHistoryAdapter = WaterHistoryAdapter(waterHistoryList,requireContext())
                binding.apply{
                    recyclerViewWaterHistory.adapter = waterHistoryAdapter
                    binding.waterFragmentProgressBar.visibility = View.GONE
                    txtWaterCurrentProgress.text = "$todaysWaterIntake"
                    waterProgressBar.progress = todaysWaterIntake
                    if(todaysWaterIntake>8){
                        waterProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources, R.drawable.progress_bar_drawable_red,null)
                    }
                    else{
                        waterProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources, R.drawable.progress_bar_drawable,null)
                    }
                }
            }
            else{
                Toast.makeText(requireContext(),"Failed to Retrieve Water Times", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addWaterIntake(time: Long){
        binding.waterFragmentProgressBar.visibility = View.VISIBLE
        val waterHashMap = HashMap<String,Long>()
        waterHashMap["time"] = time
        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("waterIntake").document().set(waterHashMap).addOnCompleteListener { addWaterIntakeTask ->
            if(addWaterIntakeTask.isSuccessful){
                getWaterIntakes()
            }
            else{
                Toast.makeText(requireContext(),"Failed to Add Water Glass",Toast.LENGTH_SHORT).show()
            }
        }
    }

}