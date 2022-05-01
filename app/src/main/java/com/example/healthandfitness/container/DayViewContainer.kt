package com.example.healthandfitness.container

import android.view.View
import com.example.healthandfitness.databinding.SingleCalenderDayBinding
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
     val textView = SingleCalenderDayBinding.bind(view).calendarDateText
}