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
import com.example.books.R
import com.example.books.database.User
import com.example.books.databinding.EditFileFragmentBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val TAG = "EditFileFragment"
const val REQUEST_CODE_IMAGE =1
class EditFileFragment : Fragment() {



    private val viewModel by lazy { ViewModelProvider(this)[EditFileViewModel::class.java] }

    private lateinit var binding:EditFileFragmentBinding
     private lateinit var user: User
     val imageRef = Firebase.storage.reference
    val storeImageInFirestore=FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

      var cruFile :Uri? = null
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

        binding.changePhotoBtn.setOnClickListener {
            uploadImage(user)
        }


        binding.saveBtn.setOnClickListener {
         user.username = binding.nameTv.text.toString()
//            user.age=binding.ageTv.text.toString()


            viewModel.saveUser(user)


        }



        return binding.root

    }

    private fun uploadImage(user: User)= CoroutineScope(Dispatchers.IO).launch {
  try {
      cruFile?.let {
          imageRef.child("image/$user").putFile(it).addOnSuccessListener(


              OnSuccessListener<UploadTask.TaskSnapshot> {
                  it.storage.downloadUrl.addOnSuccessListener {
                      Log.d(TAG,"Download image $it")
                      val imageUrl = it.toString()
                      user.profileImageUrl=imageUrl
                      Log.d(TAG,"cannot save link")
                      Log.d(TAG,"Download image $imageUrl")

                  }
              }

          )?.addOnFailureListener(OnFailureListener {
              Log.d(TAG,"error url ")
          })
          withContext(Dispatchers.Main){
              Toast.makeText(context,"true save",Toast.LENGTH_SHORT).show()
      }
  }}
  catch (e:Exception){
      withContext(Dispatchers.Main){
          Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
      }
  }


    }

    fun saveUserProfileUrl(){

      val uid= FirebaseAuth.getInstance().uid





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE)

            data?.data.let {

               cruFile = it
                binding.profileImage.setImageURI(it)

            }


    }







}