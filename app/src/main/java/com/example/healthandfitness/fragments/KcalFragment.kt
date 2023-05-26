package com.example.healthandfitness.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.Constants
import com.example.healthandfitness.R
import com.example.healthandfitness.activities.SelectFoodFromListActivity
import com.example.healthandfitness.adapter.FoodKcalAdapter
import com.example.healthandfitness.databinding.FragmentKcalBinding
import com.example.healthandfitness.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class KcalFragment : Fragment() {

    lateinit var binding: FragmentKcalBinding
    lateinit var intakeAdapter: FoodKcalAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var todaysCalories = 0
    private var todaysDateString = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKcalBinding.inflate(inflater)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        Constants.kcalFragmentReference = this@KcalFragment

        val todayDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        todaysDateString = formatter.format(todayDate)

        db.collection("users").document(firebaseAuth.currentUser?.uid!!).get().addOnCompleteListener { getUserDataTask->
            if(getUserDataTask.isSuccessful){
                Constants.userCalorieGoal = getUserDataTask.result?.data?.get("calorieIntake").toString().toInt()
                Log.d("Vinesh","Calorie Goal: ${Constants.userCalorieGoal}")
                binding.apply{
                    txtKcalTotalProgress.text = "${Constants.userCalorieGoal}"
                    kcalProgressBar.max = Constants.userCalorieGoal
                }
                getUsersCalorieIntakeList()
            }
            else{
                Toast.makeText(requireContext(),"Please Check your Internet connection",Toast.LENGTH_SHORT).show()
                requireActivity().finishAffinity()
            }
        }

        intakeAdapter = FoodKcalAdapter(Constants.usersCalorieIntakeList,requireContext())

        binding.apply {

            kcalProgressBar.visibility = View.VISIBLE

            recyclerViewKcalIntake.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = intakeAdapter
            }

            btnAddKcalCounter.setOnClickListener {

                Intent(requireContext(),SelectFoodFromListActivity::class.java).also{
                    it.putExtra("fragment","kcal")
                    startActivity(it)
                }

            }

        }

        return binding.root
    }

    private fun getUsersCalorieIntakeList(){
        binding.kcalFragmentProgressBar.visibility = View.VISIBLE
        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("todaysIntake").get().addOnCompleteListener { getIntakeList ->
            Constants.usersCalorieIntakeList.clear()
            if((getIntakeList.isSuccessful) and (getIntakeList.result.size()>0)){
                getIntakeList.result.documents.forEach { doc->
                    todaysCalories = 0
                    if(doc["date"]==todaysDateString){
                        val docId = doc.id
                        val name = doc["name"].toString()
                        val carbs = doc["carbs"].toString().toDouble()
                        val fats = doc["fats"].toString().toDouble()
                        val fiber = doc["fiber"].toString().toDouble()
                        val protein = doc["protein"].toString().toDouble()
                        val calories = doc["calories"].toString().toInt()
                        val image = doc["image"].toString()
                        todaysCalories+=calories
                        Constants.usersCalorieIntakeList.add(Food(docId, name, calories, carbs, fats, fiber, protein, image))
                        Log.d("Vinesh","List Length ${Constants.usersCalorieIntakeList.size}")
                    }
                    else{
                        todaysCalories = 0
                        binding.apply {
                            txtKcalCurrentProgress.text = "$todaysCalories"
                            kcalFragmentProgressBar.visibility = View.GONE
                            kcalProgressBar.progress = todaysCalories
                        }
                        Constants.usersCalorieIntakeList.clear()
                        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("todaysIntake").get().addOnCompleteListener {
                            it.result.documents.forEach { docToDelete ->
                                docToDelete.reference.delete()
                            }
                        }
                        return@addOnCompleteListener

                    }
                }
            }
            Log.d("Vinesh","Today's Calories: $todaysCalories")
            binding.apply {
                txtKcalCurrentProgress.text = "$todaysCalories"
                kcalFragmentProgressBar.visibility = View.GONE
                kcalProgressBar.progress = todaysCalories

                if(todaysCalories>Constants.userCalorieGoal){
                    requireActivity().apply {
                        kcalProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar_drawable_red,null)
                    }
                }
                else{
                    requireActivity().apply {
                        kcalProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar_drawable,null)
                    }
                }
            }
            intakeAdapter.notifyDataSetChanged()
        }
    }

//    fun addFoodToIntakeList(position: Int){
//        val food = HashMap<String,Any>()
//        food["name"] = Constants.allAvailableFoodsList[position].name
//        food["calories"] = Constants.allAvailableFoodsList[position].calories
//        food["carbs"] = Constants.allAvailableFoodsList[position].carbs
//        food["protein"] = Constants.allAvailableFoodsList[position].protein
//        food["fats"] = Constants.allAvailableFoodsList[position].fats
//        food["fiber"] = Constants.allAvailableFoodsList[position].fiber
//        food["image"] = Constants.allAvailableFoodsList[position].image
//        food["date"] = todaysDateString
//        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("todaysIntake").document().set(food).addOnCompleteListener { addFoodTask ->
//            if(addFoodTask.isSuccessful){
////                Constants.usersCalorieIntakeList.add(Constants.allAvailableFoodsList[0])
////                intakeAdapter.notifyItemInserted(Constants.usersCalorieIntakeList.size-1)
////                todaysCalories += Constants.allAvailableFoodsList[position].calories
////                binding.apply{
////                    txtKcalCurrentProgress.text = "$todaysCalories"
////                    kcalFragmentProgressBar.visibility = View.GONE
////                    kcalProgressBar.progress = todaysCalories
////                    if(todaysCalories>Constants.userCalorieGoal){
////                        kcalProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar_drawable_red,null)
////                    }
////                    else{
////                        kcalProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar_drawable,null)
////                    }
////                }
//                getUsersCalorieIntakeList()
//            }
//            else{
//                Toast.makeText(requireContext(),"Please Check your Internet connection",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    fun removeFoodFromList(position: Int){

        binding.kcalFragmentProgressBar.visibility = View.VISIBLE
        val docId = Constants.usersCalorieIntakeList[position].docId
        db.collection("users").document(firebaseAuth.currentUser?.uid!!).collection("todaysIntake").get().addOnCompleteListener { getAllDocs ->
            if(getAllDocs.isSuccessful){
                getAllDocs.result.documents.forEach { doc->
                    if(docId==doc.id){
                        doc.reference.delete()
                        todaysCalories -= Constants.usersCalorieIntakeList[position].calories
                        binding.apply {
                            kcalFragmentProgressBar.visibility = View.GONE
                            kcalProgressBar.progress = todaysCalories
                            txtKcalCurrentProgress.text = todaysCalories.toString()
                            if(kcalProgressBar.progress>Constants.userCalorieGoal){
                                kcalProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar_drawable_red,null)
                            }
                            else{
                                kcalProgressBar.progressDrawable = ResourcesCompat.getDrawable(resources,R.drawable.progress_bar_drawable,null)
                            }
                        }
                        Constants.usersCalorieIntakeList.removeAt(position)
                        intakeAdapter.notifyDataSetChanged()

                        return@addOnCompleteListener

                    }
                }
                binding.kcalFragmentProgressBar.visibility = View.GONE
            }
            else{
                Toast.makeText(requireContext(),"Please Check your Internet connection",Toast.LENGTH_SHORT).show()
            }
        }

    }

}