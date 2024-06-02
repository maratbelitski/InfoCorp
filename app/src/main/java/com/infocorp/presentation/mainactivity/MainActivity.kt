package com.infocorp.presentation.mainactivity

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
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
import com.infocorp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navHostFragment by lazy {
        supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
    }

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firstTimeLangInit { onInitLanguage() }
        setContentView(binding.root)

        MobileAds.initialize(this)

        onInitThemeParams()
        onBottomNavigation()
        onListeners()
        onObservers()
    }

    private fun onObservers() {
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

    fun onInitLanguage() {
        val lang = viewModel.getLanguageParams()
        val localeListCompat = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(localeListCompat)
        Locale.setDefault(Locale(lang))
        Locale.forLanguageTag(lang)
    }

    private fun onListeners() {
        binding.fab.setOnClickListener {

            val navController = navHostFragment.navController
            navController.navigate(R.id.userCorpGeneralFragment)
        }

        binding.adView.adListener = (object : AdListener() {
            override fun onAdClosed() {
                binding.adView.visibility = View.GONE
                binding.progress.shimmer.visibility = View.GONE
            }

            override fun onAdLoaded() {
                binding.progress.shimmer.visibility = View.INVISIBLE
                binding.adView.visibility = View.VISIBLE
            }
        })

        binding.refreshLayout.setOnRefreshListener {
            refreshActivity()
        }
    }

    private fun refreshActivity() {
        with(binding){
            refreshLayout.isRefreshing = true
            recreate()
            refreshLayout.isRefreshing = false
        }
    }

    private fun onBottomNavigation() {
        binding.bottomMenu.background = null

        val navController: NavController = navHostFragment.navController
        binding.bottomMenu.setupWithNavController(navController)
    }

    fun disableBottomMenu() {
        binding.coordinatorLayout.visibility = View.GONE
    }

    fun enableBottomMenu() {
        binding.coordinatorLayout.visibility = View.VISIBLE
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
        val result =
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        return result
    }
}