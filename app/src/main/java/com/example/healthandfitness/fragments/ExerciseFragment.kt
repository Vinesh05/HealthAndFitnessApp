package com.example.healthandfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.ExerciseHistoryAdapter
import com.example.healthandfitness.container.DayViewContainer
import com.example.healthandfitness.container.MonthHeaderViewContainer
import com.example.healthandfitness.databinding.FragmentExerciseBinding
import com.example.healthandfitness.model.ExerciseHistory
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.healthandfitness.activities.TodaysExercisePlanActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.yearMonth
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle

class ExerciseFragment : Fragment() {

    private lateinit var binding: FragmentExerciseBinding
    private lateinit var exerciseHistoryList: ArrayList<ExerciseHistory>
    private lateinit var exerciseHistoryAdapter: ExerciseHistoryAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        exerciseHistoryList = ArrayList()

        binding.apply {
            exerciseHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            val daysOfWeek = arrayOf("Mo","Tu","We","Th","Fr","Sa","Su")
            calHeaderLayoutExerciseFragment.children.forEachIndexed { index, view ->
                (view as TextView).apply {
                    text = daysOfWeek[index]
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.secondary))
                }
            }

            cardTodaysExercisePlan.setOnClickListener {
                Intent(requireContext(),TodaysExercisePlanActivity::class.java).also {
                    startActivity(it)
                }
            }

        }

        getExerciseHistory()

        return binding.root
    }

    private fun getExerciseHistory(){
        exerciseHistoryList.clear()
        binding.progressBarExerciseFragment.visibility = View.VISIBLE
        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("exerciseHistory").get().addOnCompleteListener { getAllExHistory->
            if(getAllExHistory.isSuccessful){
                getAllExHistory.result.documents.forEach { docs->
                    Log.d("Vinesh","Get all exercises for each")
                    val time = docs.data?.get("time").toString().toLong()
                    val calBurnt = docs.data?.get("calBurnt").toString().toInt()
                    exerciseHistoryList.add(ExerciseHistory(time,calBurnt))
                }
//                Log.d("Vinesh","Exercise History List: ${exerciseHistoryList[0].dateAndTime}")
                exerciseHistoryAdapter = ExerciseHistoryAdapter(exerciseHistoryList,requireContext())
                binding.apply{
                    exerciseHistoryRecyclerView.adapter = exerciseHistoryAdapter
                    calendarViewExerciseFragment.apply{
                        dayBinder = object : DayBinder<DayViewContainer> {
                            override fun create(view: View) = DayViewContainer(view)

                            override fun bind(container: DayViewContainer, day: CalendarDay) {

                                if(day.owner==DayOwner.THIS_MONTH){
                                    exerciseHistoryList.forEach {
                                        val dt = Instant.ofEpochMilli(it.dateAndTime).atZone(ZoneId.systemDefault()).toLocalDate()
                                        if(day.date == dt){
                                            container.textView.setBackgroundResource(R.drawable.selected_date_background)
                                            container.textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                                        }
                                    }
                                    if(day.date.dayOfWeek.value==7){
                                        container.textView.setTextColor(Color.RED)
                                    }
                                    container.textView.text = day.date.dayOfMonth.toString()
                                }

                            }
                        }
//                        monthHeaderResource = R.layout.calendar_header
//                        monthHeaderBinder = object: MonthHeaderFooterBinder<MonthHeaderViewContainer>{
//                            override fun bind(container: MonthHeaderViewContainer, month: CalendarMonth) {
//
//                            }
//
//                            override fun create(view: View): MonthHeaderViewContainer = MonthHeaderViewContainer(view)
//
//                        }
                        val currentMonth = YearMonth.now()
                        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
                        setup(currentMonth, currentMonth, firstDayOfWeek)
                        scrollToMonth(currentMonth)
                    }
                    progressBarExerciseFragment.visibility = View.GONE
                }
            }
            else{
                binding.progressBarExerciseFragment.visibility = View.GONE
                Toast.makeText(requireContext(),"Check your internet connection",Toast.LENGTH_SHORT).show()
            }
        }
    }

}