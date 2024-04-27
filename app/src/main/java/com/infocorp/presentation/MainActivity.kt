package com.infocorp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.infocorp.R
import com.infocorp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), UpdateBottomMenu {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
       // binding.coordinatorLayout.visibility = View.GONE
    }

    override fun enableBottomMenu() {
//        binding.coordinatorLayout.visibility = View.VISIBLE
        binding.bottomMenu.menu.getItem(0).isChecked = true
    }
}