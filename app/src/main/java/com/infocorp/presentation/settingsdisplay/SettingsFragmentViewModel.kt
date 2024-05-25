package com.infocorp.presentation.settingsdisplay

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewModel @Inject constructor(
    private val sharedPref: SharedPreferences
) : ViewModel() {

    fun setThemeParams(themeParams:String){
        sharedPref.edit().putString("themeParams",themeParams).apply()
    }
}