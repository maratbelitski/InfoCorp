package com.infocorp.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import com.infocorp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val adRequest: AdRequest,
    private val sharedPref: SharedPreferences
) : ViewModel() {
    private var _banner = MutableStateFlow(adRequest)
    val banner: StateFlow<AdRequest>
        get() = _banner.asStateFlow()

    fun getThemeParams(): String {
        return sharedPref.getString(Constants.THEME_PARAMS_PREFERENCES.value , "") ?: ""
    }

    fun getLanguageParams(): String {
        return sharedPref.getString(Constants.LANG_PARAMS_PREFERENCES.value, "") ?: ""
    }
}

