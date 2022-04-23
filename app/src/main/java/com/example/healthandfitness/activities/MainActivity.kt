package com.example.healthandfitness.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthandfitness.R
import com.example.healthandfitness.databinding.ActivityMainBinding
import com.example.healthandfitness.fragments.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val kcalFragment = KcalFragment()
    val dietPlanFragment = DietPlanFragment()
    val exerciseFragment = ExerciseFragment()
    val blogsFragment = BlogsFragment()
    val waterFragment = WaterReminderFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()

    }

    private fun setUpBottomNavigation(){

        binding.apply{
            kcalBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, kcalFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_kcal_selected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            dietPlanBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, dietPlanFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_diet_plan_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            exerciseBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, exerciseFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_exercise_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            blogsBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, blogsFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_blogs_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    waterReminderBottomNavigation.setImageResource(R.drawable.ic_water_unselected)
                }
            }
            waterReminderBottomNavigation.apply{
                setOnClickListener {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutMainActivity, waterFragment)
                    transaction.commit()
                    setImageResource(R.drawable.ic_water_selected)
                    kcalBottomNavigation.setImageResource(R.drawable.ic_kcal_unselected)
                    dietPlanBottomNavigation.setImageResource(R.drawable.ic_diet_plan_unselected)
                    exerciseBottomNavigation.setImageResource(R.drawable.ic_exercise_unselected)
                    blogsBottomNavigation.setImageResource(R.drawable.ic_blogs_unselected)
                }
            }
            kcalBottomNavigation.callOnClick()
        }

    }

}