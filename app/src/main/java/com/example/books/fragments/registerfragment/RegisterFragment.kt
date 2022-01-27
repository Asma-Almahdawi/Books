package com.example.books.fragments.registerfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.books.database.User
import com.example.books.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
binding= RegisterFragmentBinding.inflate(layoutInflater)
        binding.registerBtn.setOnClickListener {
            val email = binding.emailTv.text.toString()
            val password = binding.passwordTv.text.toString()

            when {
                email.isEmpty() -> showToast("enter email")
                password.isEmpty() -> showToast("enter password")
                else -> registerUser(email, password)
            }
        }

        return binding.root

    }


    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(userId = auth.currentUser!!.uid)
                    val firestoreDB = FirebaseFirestore.getInstance()
                    firestoreDB.collection("users").document(auth.currentUser!!.uid).set(user)
                    auth.signOut()
                    findNavController().popBackStack()
                    showToast("good job")
                } else {
                    Log.e(TAG, "there was something wrong", task.exception)
                }
            }
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

    }
}