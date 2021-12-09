package com.example.books.fragments.homepagefragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.books.R
import com.example.books.databinding.HomePageFragmentBinding

class HomePageFragment : Fragment() {

   private lateinit var binding: HomePageFragmentBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding= HomePageFragmentBinding.inflate(layoutInflater)
        binding.booksRv.layoutManager=LinearLayoutManager(context)
        return binding.root
    }



}