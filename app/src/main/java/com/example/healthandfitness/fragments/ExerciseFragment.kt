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
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.children
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater)

        exerciseHistoryList = ArrayList()
        exerciseHistoryList.add(ExerciseHistory(1651241155338L,219))
        exerciseHistoryList.add(ExerciseHistory(1649056872000L,219))
        exerciseHistoryList.add(ExerciseHistory(1651241181224L,219))
        exerciseHistoryList.add(ExerciseHistory(1649575272000L,219))
        exerciseHistoryList.add(ExerciseHistory(1651241194263L,219))

        exerciseHistoryAdapter = ExerciseHistoryAdapter(exerciseHistoryList,requireContext())

        binding.apply {
            exerciseHistoryRecyclerView.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = exerciseHistoryAdapter
            }

            val daysOfWeek = arrayOf("Mo","Tu","We","Th","Fr","Sa","Su")
            calHeaderLayoutExerciseFragment.children.forEachIndexed { index, view ->
                (view as TextView).apply {
                    text = daysOfWeek[index]
                    setTextColor(ContextCompat.getColor(requireContext(),R.color.secondary))
                }
            }

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
//                monthHeaderResource = R.layout.calendar_header
//                monthHeaderBinder = object: MonthHeaderFooterBinder<MonthHeaderViewContainer>{
//                    override fun bind(container: MonthHeaderViewContainer, month: CalendarMonth) {
//
//                    }
//
//                    override fun create(view: View): MonthHeaderViewContainer = MonthHeaderViewContainer(view)
//
//                }
                val currentMonth = YearMonth.now()
                val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
                setup(currentMonth, currentMonth, firstDayOfWeek)
                scrollToMonth(currentMonth)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}