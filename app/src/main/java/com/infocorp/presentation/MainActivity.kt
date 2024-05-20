package com.infocorp.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize

import com.infocorp.R
import com.infocorp.databinding.ActivityMainBinding
import com.infocorp.presentation.usercorpgeneraldisplay.UserCorpGeneralFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBottomNavigation()
        onListeners()
    }

    private fun onListeners() {
        binding.fab.setOnClickListener {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
            val navController = navHostFragment.navController

            navController.navigate(R.id.userCorpGeneralFragment)
        }
    }

    private fun onBottomNavigation() {
        binding.bottomMenu.background = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        binding.bottomMenu.setupWithNavController(navController)
    }

    fun disableBottomMenu() {
        binding.coordinatorLayout.visibility = View.GONE
    }

    fun enableBottomMenu() {
        binding.coordinatorLayout.visibility = View.VISIBLE
    }
}