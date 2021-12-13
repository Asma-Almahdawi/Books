package com.example.books.fragments.editfilefragment

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.books.R
import com.example.books.database.User
import com.example.books.databinding.EditFileFragmentBinding

const val REQUEST_CODE_IMAGE =1
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

        binding.profileImage.setOnClickListener {

          Intent(Intent.ACTION_GET_CONTENT).also {

          it.type= "image/*"

              startActivityForResult(it,REQUEST_CODE_IMAGE)

          }

        }

        binding.saveBtn.setOnClickListener {
         user.username = binding.nameTv.text.toString()
//            user.age=binding.ageTv.text.toString()


            viewModel.saveUser(user)


        }



        return binding.root

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE)

            data?.data.let {



            }
    }







}