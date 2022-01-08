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
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.books.R
import com.example.books.commentFragment.Worker
import com.example.books.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

private const val TAG = "LoginFragment"
private const val WORK = "WORK"
class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth:FirebaseAuth
    

//    companion object {
//        fun newInstance() = LoginFragment()
//    }
//
//    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//startNotificationWorker()
        auth= FirebaseAuth.getInstance()

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding= LoginFragmentBinding.inflate(layoutInflater)

        binding.loginBtn.setOnClickListener {
        val email=binding.emailLoginTv.text.toString()
        val password=binding.passwordLoginTv.text.toString()

        when{

            email.isEmpty()->showToast("enter username")
            password.isEmpty()->showToast("enter password")
            else->loginUser(email, password)




        }

//findNavController().navigate(R.id.action_registerFragment_to_loginFragment
       
        }


        return binding.root
    }

    private fun showToast(s: String) {

    }
//    val onShowNotification = object : BroadcastReceiver(){
//        override fun onReceive(context: Context?, intent: Intent?) {
//            Log.d(TAG , "hi im awake")
//            resultCode = Activity.RESULT_CANCELED
//        }
//
//    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){

        }
//
//        IntentFilter(Worker.ACTION_SHOW_NOTIFICATION).also {
//            requireContext().registerReceiver(onShowNotification
//                ,it,
//                Worker.PERM_PRIVATE,
//                null
//            )
//        }
    }



//    override fun onStop() {
//        super.onStop()
//        requireContext().unregisterReceiver(onShowNotification)
//    }
    private fun startNotificationWorker() {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequest
                .Builder(Worker::class.java, 6, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(requireContext())
                .enqueueUniquePeriodicWork(
                    WORK,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )

        }




    private  fun loginUser(email:String , password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if (task.isSuccessful) {
//                    startNotificationWorker()
          findNavController().navigate(R.id.action_loginFragment_to_booksFragment)
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(context, "Authentication Done.",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }


            }

    }




}

