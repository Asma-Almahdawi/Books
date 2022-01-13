package com.example.books.fragments.loginfragment


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.books.R
import com.example.books.commentFragment.Constants
import com.example.books.commentFragment.LoginValidation
import com.example.books.commentFragment.Validation
import com.example.books.commentFragment.Worker
import com.example.books.databinding.LoginFragmentBinding
import com.example.books.fragments.registerfragment.RegistrationUtil
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

private const val TAG = "LoginFragment"
private const val WORK = "WORK"
class LoginFragment : Fragment() {

    val loginViewModel by lazy { ViewModelProvider(this) [LoginViewModel::class.java] }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= FirebaseAuth.getInstance()

//        startNotificationWorker()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding= LoginFragmentBinding.inflate(layoutInflater)

        binding.loginBtn.setOnClickListener {
        val email=binding.emailLoginTv.text.toString()
        val password=binding.passwordLoginTv.text.toString()

        when(RegistrationUtil.validateRegistrationInput(email, password)){

            Constants.usernameOrPasswordIsEmpty->
            {showToast(" enter email or password")
                Log.d(TAG, "onCreateView: enter email or password")
            }

            "you are logged"-> {

              val isSuccess = loginViewModel.loginUser(email, password)
                startNotificationWorker()

                if (isSuccess){
                    findNavController().popBackStack()
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
        if(currentUser != null){

        }

    }

//    private val EMAILE_PATTREN ="[a-zA-Z-9._-]+@[a-z]+\\.+[a-z]+"
//    fun email(email:String):Boolean{
//
//        if(email.matches(EMAILE_PATTREN.toRegex()))
//
//            return true
//        return false
//    }

}

