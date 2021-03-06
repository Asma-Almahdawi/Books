package com.example.books.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.books.MainActivity
import com.example.books.QueryPreferences
import com.example.books.R
import com.example.books.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {


    private lateinit var binding: FragmentSettingBinding
    val viewModel: SettingViewModel by activityViewModels()
    lateinit var myPreference: QueryPreferences

    private val languageList: Array<String> = arrayOf("en", "ar")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myPreference = context?.let { QueryPreferences(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentSettingBinding.inflate(layoutInflater)

        binding.spinner.adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, languageList) }
        val lang: String? = myPreference.getLoginCount()
        val index: Int = languageList.indexOf(lang)

        if (index >= 0) {
            binding.spinner.setSelection(index)
        }
        binding.setBtn.setOnClickListener {
            myPreference.setLoginCount(languageList[binding.spinner.selectedItemPosition])
            startActivity(Intent(requireContext(),MainActivity::class.java))
            activity?.finish()
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment2)
        }

        binding.signOutBtn.setOnClickListener {

            viewModel.signOut()
            findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
        }

        return binding.root
    }
}