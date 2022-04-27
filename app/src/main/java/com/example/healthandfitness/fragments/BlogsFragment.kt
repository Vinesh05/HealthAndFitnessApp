package com.example.healthandfitness.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthandfitness.R
import com.example.healthandfitness.adapter.BlogAdapter
import com.example.healthandfitness.databinding.FragmentBlogsBinding
import com.example.healthandfitness.model.Blog

class BlogsFragment : Fragment() {

    private lateinit var binding: FragmentBlogsBinding
    private lateinit var blogList: ArrayList<Blog>
    private lateinit var blogAdapter: BlogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogsBinding.inflate(inflater)

        blogList = ArrayList()
        blogList.add(Blog("Blog1 Heading","The wave crashed and hit the sandcastle head-on. The sandcastle began to melt under the waves force and as the wave receded, half the sandcastle was gone. The next wave hit, not quite as strong, but still managed to cover the remains of the sandcastle and take more of it away. The third wave, a big one, crashed over the sandcastle completely covering and engulfing it. When it receded, there was no trace the sandcastle ever existed and hours of hard work disappeared forever.",""))
        blogList.add(Blog("Blog2 Heading","The wave crashed and hit the sandcastle head-on. The sandcastle began to melt under the waves force and as the wave receded, half the sandcastle was gone. The next wave hit, not quite as strong, but still managed to cover the remains of the sandcastle and take more of it away. The third wave, a big one, crashed over the sandcastle completely covering and engulfing it. When it receded, there was no trace the sandcastle ever existed and hours of hard work disappeared forever.",""))
        blogList.add(Blog("Blog3 Heading","The wave crashed and hit the sandcastle head-on. The sandcastle began to melt under the waves force and as the wave receded, half the sandcastle was gone. The next wave hit, not quite as strong, but still managed to cover the remains of the sandcastle and take more of it away. The third wave, a big one, crashed over the sandcastle completely covering and engulfing it. When it receded, there was no trace the sandcastle ever existed and hours of hard work disappeared forever.",""))
        blogList.add(Blog("Blog4 Heading","The wave crashed and hit the sandcastle head-on. The sandcastle began to melt under the waves force and as the wave receded, half the sandcastle was gone. The next wave hit, not quite as strong, but still managed to cover the remains of the sandcastle and take more of it away. The third wave, a big one, crashed over the sandcastle completely covering and engulfing it. When it receded, there was no trace the sandcastle ever existed and hours of hard work disappeared forever.",""))

        blogAdapter = BlogAdapter(blogList,requireContext())

        binding.apply{
            recyclerViewBlogsFragment.apply{
                layoutManager = LinearLayoutManager(requireContext())
                adapter = blogAdapter
            }
        }

        return binding.root
    }

}