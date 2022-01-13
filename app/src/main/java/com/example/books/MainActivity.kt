package com.example.books

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.books.databinding.ActivityMainBinding
import com.google.android.gms.common.util.CollectionUtils.setOf
import java.util.*


class MainActivity : AppCompatActivity() {

    val mainActivityViewModel by lazy { ViewModelProvider (this) [MainActivityViewModel::class.java] }
    lateinit var binding:ActivityMainBinding
    private lateinit var myPreferences: QueryPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttomNavMenu.background=null
        binding.buttomNavMenu.menu.getItem(2).isEnabled = false

        val bottomNavigationView = binding.buttomNavMenu

        val navController = findNavController(R.id.container)

        bottomNavigationView.setupWithNavController(navController)
         navController.addOnDestinationChangedListener { _, destination, _ ->

             when(destination.id){

                 R.id.loginFragment -> {
                     binding.buttomNavMenu.visibility = View.GONE
                     binding.fab.visibility=View.GONE
                     binding.bottomAppBar.visibility=View.GONE
                 }
                 R.id.registerFragment -> {
                     binding.buttomNavMenu.visibility = View.GONE
                     binding.fab.visibility=View.GONE
                     binding.bottomAppBar.visibility=View.GONE
                 }
                 else -> {
                     binding.buttomNavMenu.visibility = View.VISIBLE
                     binding.fab.visibility=View.VISIBLE
                     binding.bottomAppBar.visibility=View.VISIBLE
                 }
             }

         }

        binding.fab.setOnClickListener {

         findNavController(R.id.container).navigate(R.id.booksFragment)

        }
//        setupActionBarWithNavController(navController, AppBarConfiguration(setOf( R.layout.home_page_fragment,R.layout.like_page_fragment,R.layout.chat_page_fragment,R.layout.profile_fragment)))



    }

    override fun attachBaseContext(newBase: Context?) {
        myPreferences = QueryPreferences(newBase!!)
        val lang : String? = myPreferences.getLoginCount()
        super.attachBaseContext(lang?.let { MyContextWrapper.wrap(newBase, it) })


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
