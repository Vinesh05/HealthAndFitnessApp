package com.example.healthandfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthandfitness.R
import com.example.healthandfitness.databinding.ActivityBlogBinding

class BlogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blogTitle = intent.getStringExtra("title")
        val blogDescription = intent.getStringExtra("description")
        val blogImage = intent.getStringExtra("image")

        binding.apply {
            txtBlogTitleBlogActivity.text = blogTitle.toString()
            txtBlogDescriptionBlogActivity.text = blogDescription.toString()
        }

    }
}