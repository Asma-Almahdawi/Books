package com.example.books.fragments.profilepagefragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.books.R
import com.example.books.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment() {


private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= ProfileFragmentBinding.inflate(layoutInflater)
        binding.booksUserRv.layoutManager=LinearLayoutManager(context)

        return binding.root
    }


//    private inner class ProfileHolder():RecyclerView.ViewHolder()

}