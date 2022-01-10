package com.example.books.fragments.chatfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.books.R
import com.example.books.databinding.ChatPageFragmentBinding

class ChatPageFragment : Fragment() {
private lateinit var binding: ChatPageFragmentBinding
    companion object {
        fun newInstance() = ChatPageFragment()
    }

    private lateinit var viewModel: ChatPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChatPageFragmentBinding.inflate(layoutInflater)

        binding.textVi.setOnClickListener {
            findNavController().navigate(R.id.action_chatPageFragment_to_settingFragment)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}