package com.example.books.fragments.profilepagefragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.books.R
import com.example.books.database.User
import com.example.books.databinding.ProfileFragmentBinding
import com.example.books.fragments.homepagefragment.HomePageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment() {

    private val profileViewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }

private lateinit var binding: ProfileFragmentBinding

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        userId=args.userId
        user=User()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= ProfileFragmentBinding.inflate(layoutInflater)
//        binding.userImage.load(user.profileImageUrl)
//        binding.usernameTv.text=user.username

        lifecycleScope.launch {

            profileViewModel.getUserData().observe(

                viewLifecycleOwner,
                Observer {
                    Log.d(TAG, "onCreateView: $it")
                    binding.usernameTv.text=it.username
                    binding.userImage.load(it.profileImageUrl)
                    binding.bioTv.text=it.bio
                    Log.d(TAG, "onCreateView: ${user.profileImageUrl}")
                }

            )

        }

//        getProfileUserData()

//       lifecycleScope.launch {
////           user=User(userId)
//           profileViewModel.getUserData(user.userId)?:User()
//       }

        binding.editProfileBtn.setOnClickListener {
//               lifecycleScope.launch {
////                   binding.userImage.load(user.profileImageUrl)
////                   binding.usernameTv.text=user.username
//                   profileViewModel.getUserData()
//               }
//            getProfileUserData()
            findNavController().navigate(R.id.action_profileFragment_to_editFileFragment)
        }

        return binding.root
    }

    override fun onResume() {
//binding.langBtn.setOnClickListener {
    changeLanguage()
//}

        super.onResume()
    }
    fun changeLanguage(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val language = sharedPreferences.getString("language","bak")
        Toast.makeText(context,language, Toast.LENGTH_SHORT).show()
        if(language=="English"){
            Toast.makeText(context,"English", Toast.LENGTH_SHORT).show()
            language("")
        }else if(language=="Arabic"){
            Toast.makeText(context,"Arabic", Toast.LENGTH_SHORT).show()
            language("ar")
        }
    }


    fun language(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = getResources()
        val configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
    }





//    private inner class ProfileHolder():RecyclerView.ViewHolder()

}