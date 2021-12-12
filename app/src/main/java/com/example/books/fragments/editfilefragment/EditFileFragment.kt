package com.example.books.fragments.editfilefragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.books.R
import com.example.books.database.User
import com.example.books.databinding.EditFileFragmentBinding

class EditFileFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this)[EditFileViewModel::class.java] }

    private lateinit var binding:EditFileFragmentBinding
     private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user= User()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= EditFileFragmentBinding.inflate(layoutInflater)

        binding.saveBtn.setOnClickListener {
            binding.nameTv.text.toString()

            viewModel.saveUser(user)


        }

        return binding.root

    }







}