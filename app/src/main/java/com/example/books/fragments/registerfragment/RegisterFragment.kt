package com.example.books.fragments.registerfragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.books.R
import com.example.books.database.User
import com.example.books.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "RegisterFragment"
class RegisterFragment : Fragment() {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var auth: FirebaseAuth
//    private lateinit var database:DatabaseReference
//    private lateinit var user:User

//    companion object {
//        fun newInstance() = RegisterFragment()
//    }
//
//    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth= FirebaseAuth.getInstance()
//
//        binding.registerBtn.setOnClickListener {
//
//            val username=binding.usernameTv.text.toString()
//            val email = binding.emailTv.text.toString()
//            val password=binding.passwordTv.text.toString()
//
//            database= FirebaseDatabase.getInstance().getReference("Users")
//            val User = User(username,email,password)
//
//            database.child(username).setValue(User).addOnSuccessListener {
//
//                binding.usernameTv.text.clear()
//                binding.emailTv.text.clear()
//                binding.usernameTv.text.clear()
//
//                Toast.makeText(context,"saved" , Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//
//                Toast.makeText(context,"failed" , Toast.LENGTH_SHORT).show()
//            }
//
//
//        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     binding= RegisterFragmentBinding.inflate(layoutInflater)

//Log.d(TAG,"ERROR")
//        binding.registerBtn.setOnClickListener {
//
//            val username=binding.usernameTv.text.toString()
//            val email = binding.emailTv.text.toString()
//            val password=binding.passwordTv.text.toString()
//
//            database= FirebaseDatabase.getInstance().getReference("Users")
//            val User = User(username,email,password)
////            database.child(username).setValue(User).add
//
//            database.child(username).setValue(User).addOnSuccessListener {
//
//                binding.usernameTv.text.clear()
//                binding.emailTv.text.clear()
//                binding.usernameTv.text.clear()
//
//                Toast.makeText(context,"saved" , Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//
//                Toast.makeText(context,"failed" , Toast.LENGTH_SHORT).show()
//            }
//
//
//        }
        binding.registerBtn.setOnClickListener {
//
            val username=binding.usernameTv.text.toString()
            val email = binding.emailTv.text.toString()
            val password=binding.passwordTv.text.toString()

            when{

                username.isEmpty()->showToast("enter username")
                email.isEmpty()->showToast("enter email")
                password.isEmpty()->showToast("enter password")
                else->registerUser(username,email, password)


            }



//
//            database= FirebaseDatabase.getInstance().getReference("Users")
//            val User = User(username,email,password)
////            database.child(username).setValue(User).add
//
//            database.child(username).setValue(User).addOnSuccessListener {
//
//                binding.usernameTv.text.clear()
//                binding.emailTv.text.clear()
//                binding.usernameTv.text.clear()
//
//                Toast.makeText(context,"saved" , Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//
//                Toast.makeText(context,"failed" , Toast.LENGTH_SHORT).show()
//            }
//
//
//        }


    }




        return binding.root


}

//    private fun registerUser(username:String,email: String, password: String) {
//        auth.createUserWithEmailAndPassword(email,password)
//            .addOnCompleteListener { task->
//                if (task.isSuccessful){
//                    FirebaseFirestore.getInstance().collection("users").document(auth.currentUser?.uid!!).set(
//                        hashMapOf("name" to binding.usernameTv.text,
//                                    "email" to binding.emailTv.text)
//                    )
//                    showToast("good job")
//                }else{
//
//                    Log.e(TAG , "there was something wrong",task.exception)
//                }
//
//            }
private fun registerUser(username:String,email: String, password: String) {
    auth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener { task->
            if (task.isSuccessful){
//                val user = User()
                val user = User()
               val  firestoreDB = FirebaseFirestore.getInstance()
                firestoreDB.collection("users").document(auth.currentUser!!.uid).set(user)
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                showToast("good job")
            }else{

                Log.e(TAG , "there was something wrong",task.exception)
            }

        }




}
    private fun showToast(msg:String){
        Toast.makeText( context, msg  ,Toast.LENGTH_LONG).show()

    }
}