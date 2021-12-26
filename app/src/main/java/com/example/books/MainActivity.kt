package com.example.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.books.databinding.ActivityMainBinding
import com.google.android.gms.common.util.CollectionUtils.setOf


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.buttomNavMenu

        val navController = findNavController(R.id.container)

        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, AppBarConfiguration(setOf( R.layout.home_page_fragment,R.layout.like_page_fragment,R.layout.chat_page_fragment,R.layout.profile_fragment)))



    }
}
