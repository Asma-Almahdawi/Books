package com.example.books.fragments.loginfragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.books.R
import com.example.books.commentFragment.Constants
import com.example.books.commentFragment.Worker
import com.example.books.databinding.LoginFragmentBinding
import com.example.books.fragments.registerfragment.RegistrationUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private const val TAG = "LoginFragment"
private const val WORK = "WORK"

class LoginFragment : Fragment() {

    val loginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = LoginFragmentBinding.inflate(layoutInflater)
        binding.createAccountTv.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailLoginTv.text.toString()
            val password = binding.passwordLoginTv.text.toString()

            when (RegistrationUtil.validateRegistrationInput(email, password)) {

                Constants.usernameOrPasswordIsEmpty -> {
                    showToast(" enter email or password")
                    Log.d(TAG, "onCreateView: enter email or password")
                }
                "you are logged" -> {
                    lifecycleScope.launch {
                        val isSuccess = loginViewModel.loginUser(email, password)
                        startNotificationWorker()
                        if (isSuccess) {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun showToast(s: String) {

    }


    private fun startNotificationWorker() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequest
            .Builder(Worker::class.java, 5, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {

        }
    }
}

