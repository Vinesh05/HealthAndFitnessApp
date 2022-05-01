package com.example.healthandfitness.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.BlogAdapter
import com.example.healthandfitness.databinding.FragmentBlogsBinding
import com.example.healthandfitness.model.Blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class BlogsFragment : Fragment() {

    private lateinit var binding: FragmentBlogsBinding
    private lateinit var blogList: ArrayList<Blog>
    private lateinit var blogAdapter: BlogAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogsBinding.inflate(inflater)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        blogList = ArrayList()
        getAllBlogs()

        binding.apply{
            recyclerViewBlogsFragment.layoutManager = LinearLayoutManager(requireContext())
        }

        return binding.root
    }

    private fun getAllBlogs(){
        binding.progressBarBlogsFragment.visibility = View.VISIBLE
        db.collection("blogs").get(Source.SERVER).addOnCompleteListener { getAllBlogsTask ->
            if(getAllBlogsTask.isSuccessful){
                Log.d("Vinesh","Blogs Task Successful")
                getAllBlogsTask.result.documents.forEach { blogs->
                    val title = blogs.data?.get("title").toString()
                    val description = blogs.data?.get("description").toString()
                    val image = blogs.data?.get("image").toString()
                    blogList.add(Blog(title,description, image))
                    Log.d("Vinesh","Title: $title")
                }
                blogAdapter = BlogAdapter(blogList,requireContext())
                binding.recyclerViewBlogsFragment.adapter = blogAdapter
                binding.progressBarBlogsFragment.visibility = View.GONE
            }
            else{
                Toast.makeText(requireContext(),"Failed to Retrieve Blogs",Toast.LENGTH_SHORT).show()
            }
        }
    }

}