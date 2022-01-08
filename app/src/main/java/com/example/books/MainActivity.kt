package com.example.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.books.databinding.ActivityMainBinding
import com.google.android.gms.common.util.CollectionUtils.setOf
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.buttomNavMenu

        val navController = findNavController(R.id.container)

        bottomNavigationView.setupWithNavController(navController)
//        setupActionBarWithNavController(navController, AppBarConfiguration(setOf( R.layout.home_page_fragment,R.layout.like_page_fragment,R.layout.chat_page_fragment,R.layout.profile_fragment)))



    }
//
//    override fun onResume() {
//
//        changeLanguage()
//        super.onResume()
//    }
//    fun changeLanguage(){
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//        val language = sharedPreferences.getString("language","bak")
//        Toast.makeText(applicationContext,language,Toast.LENGTH_SHORT).show()
//        if(language=="English"){
//            Toast.makeText(applicationContext,"English",Toast.LENGTH_SHORT).show()
//            language("")
//        }else if(language=="Arabic"){
//            Toast.makeText(applicationContext,"Arabic",Toast.LENGTH_SHORT).show()
//            language("ar")
//        }
//    }
//
//
//    fun language(language: String){
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val resources = getResources()
//        val configuration = resources.getConfiguration()
//        configuration.locale = locale
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
//    }
}
