package com.infocorp.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.MobileAds
import com.infocorp.R
import com.infocorp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MobileAds.initialize(this)

        onInitThemeParams()
        onBottomNavigation()
        onListeners()

        lifecycleScope.launch {
            viewModel.banner
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect {
                    binding.adView.loadAd(it)
                }
        }
    }

    fun onInitThemeParams() {
        when (viewModel.getThemeParams()) {
            Constants.LIGHT_MODE.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Constants.NIGHT_MODE.value -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun onListeners() {
        binding.fab.setOnClickListener {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
            val navController = navHostFragment.navController

            navController.navigate(R.id.userCorpGeneralFragment)
        }

        binding.adView.adListener = (object : AdListener() {
            override fun onAdClosed() {
                binding.adView.visibility = View.GONE
                binding.progress.visibility = View.GONE
            }

            override fun onAdLoaded() {
                binding.progress.visibility = View.INVISIBLE
                binding.adView.visibility = View.VISIBLE
            }
        })
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