package com.example.books

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.books.databinding.ActivityMainBinding
import com.google.android.gms.common.util.CollectionUtils.setOf
import com.google.android.material.bottomappbar.BottomAppBar
import java.util.*


class MainActivity : AppCompatActivity() {

    val mainActivityViewModel by lazy { ViewModelProvider(this)[MainActivityViewModel::class.java] }
    lateinit var binding: ActivityMainBinding
    private lateinit var myPreferences: QueryPreferences
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttomNavMenu.background = null

        val bottomNavigationView = binding.buttomNavMenu

        val navController = findNavController(R.id.container)

        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.loginFragment -> {
                    binding.buttomNavMenu.visibility = View.GONE
                    binding.fab.visibility = View.GONE
                    binding.bottomAppBar.visibility = View.GONE
                }
                R.id.registerFragment -> {
                    binding.buttomNavMenu.visibility = View.GONE
                    binding.fab.visibility = View.GONE
                    binding.bottomAppBar.visibility = View.GONE
                }
                else -> {
                    binding.buttomNavMenu.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                    binding.bottomAppBar.visibility = View.VISIBLE
                }
            }
        }
        binding.fab.setOnClickListener {

            onAddButtonClicked()
        }

        binding.addBook.setOnClickListener {
            findNavController(R.id.container).navigate(R.id.booksFragment)
        }

        binding.addAudioBook.setOnClickListener {
            findNavController(R.id.container).navigate(R.id.audioBookFragment)

        }
    }

    private fun onAddButtonClicked() {

        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked

    }

    private fun setAnimation(clicked: Boolean) {
        val rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim)
        val rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim)
        val fromButton = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim)
        val toButton = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)
        if (!clicked) {

            binding.addAudioBook.startAnimation(fromButton)
            binding.addBook.startAnimation(fromButton)
            binding.fab.startAnimation(rotateOpen)

        } else {
            binding.addAudioBook.startAnimation(toButton)
            binding.addBook.startAnimation(toButton)
            binding.fab.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {

        if (!clicked) {
            binding.addAudioBook.visibility = View.VISIBLE
            binding.addBook.visibility = View.VISIBLE

        }
        binding.addAudioBook.visibility = View.INVISIBLE
        binding.addBook.visibility = View.INVISIBLE

    }

    override fun attachBaseContext(newBase: Context?) {
        myPreferences = QueryPreferences(newBase!!)
        val lang: String? = myPreferences.getLoginCount()
        super.attachBaseContext(lang?.let { MyContextWrapper.wrap(newBase, it) })
    }
}
