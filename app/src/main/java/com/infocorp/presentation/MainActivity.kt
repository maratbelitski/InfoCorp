package com.infocorp.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.infocorp.R
import com.infocorp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UpdateBottomMenu {
    //private lateinit var binding: ActivityMainBinding

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bottomNavigation()
    }

    private fun bottomNavigation() {
        binding.bottomMenu.background = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        binding.bottomMenu.setupWithNavController(navController)
    }

    override fun disableBottomMenu() {
         binding.coordinatorLayout.visibility = View.GONE
    }

    override fun enableBottomMenu() {
       binding.coordinatorLayout.visibility = View.VISIBLE
        //binding.bottomMenu.menu.getItem(0).isChecked = true
    }
}