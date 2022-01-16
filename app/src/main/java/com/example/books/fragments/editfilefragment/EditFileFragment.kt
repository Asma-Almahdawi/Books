package com.example.books.fragments.editfilefragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.books.R
import com.example.books.database.User
import com.example.books.databinding.EditFileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "EditFileFragment"
const val REQUEST_CODE_IMAGE =1
class EditFileFragment : Fragment() {



    private val viewModel by lazy { ViewModelProvider(this)[EditFileViewModel::class.java] }

    private lateinit var binding:EditFileFragmentBinding
     private lateinit var user: User
    var cruFile :Uri? = null

    private val openGallery = registerForActivityResult(ActivityResultContracts.GetContent()){
         if (it != null){
             cruFile = it
         }
     }
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

            openGallery.launch("image/*")

        }
       lifecycleScope.launch {
           viewModel.getUserData().observe(

               viewLifecycleOwner, {
       user = it
                   binding.nameTv.setText(it.username)
                   binding.profileImage.load(it.profileImageUrl)
                   binding.bioTvUpdate.setText(it.bio)
               }

           )
       }


        binding.saveBtn.setOnClickListener {


            user.username = binding.nameTv.text.toString()
            user.bio=binding.bioTvUpdate.text.toString()
            binding.profileImage.load(user.profileImageUrl)
            Log.d(TAG, "onCreateView33: ${cruFile}")


            viewModel.saveUser(user)

            if (cruFile != null){
                uploadProfileImage(cruFile!!)
            }

            findNavController().navigate(R.id.action_editFileFragment_to_profileFragment)

        }



        return binding.root

    }

    private fun uploadProfileImage(curFile:Uri) {

        viewModel.uploadProfileImage(curFile)


      }







}