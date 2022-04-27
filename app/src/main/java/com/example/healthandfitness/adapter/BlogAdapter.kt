package com.example.healthandfitness.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthandfitness.R
import com.example.healthandfitness.activities.BlogActivity
import com.example.healthandfitness.model.Blog
import com.google.android.material.card.MaterialCardView
import java.lang.Exception

class BlogAdapter(private val blogList: ArrayList<Blog>,private val context: Context): RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    class BlogViewHolder(view: View): RecyclerView.ViewHolder(view){
        val mainCardView: MaterialCardView = view.findViewById(R.id.mainCardViewBlogSingleRow)
        val txtTitle: TextView = view.findViewById(R.id.txtBlogTitleBlogSingleRow)
        val txtDescription: TextView = view.findViewById(R.id.txtShortDescriptionBlogSingleRow)
        val imgBlog: ImageView = view.findViewById(R.id.imgViewBlogSingleRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_blog,parent,false)
        return BlogViewHolder(view)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        try{
            holder.apply {
                txtTitle.text = blogList[position].title
                txtDescription.text = blogList[position].description
                mainCardView.setOnClickListener {
                    Intent(context,BlogActivity::class.java).also{
                        it.putExtra("title",blogList[position].title)
                        it.putExtra("description",blogList[position].description)
                        it.putExtra("image",blogList[position].image)
                        context.startActivity(it)
                    }
                }
            }
        }
        catch(e: Exception){
            Log.d("Vinesh","Error: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return blogList.size
    }

}