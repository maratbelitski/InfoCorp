package com.infocorp.presentation.settingsdisplay

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.infocorp.presentation.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewModel @Inject constructor(
    private val sharedPref: SharedPreferences
) : ViewModel() {

    fun setThemeParams(themeParams: String) {
        sharedPref.edit().putString(Constants.THEME_PARAMS_PREFERENCES.value, themeParams).apply()
    }

    fun setLanguageParams(lang: String) {
        sharedPref.edit().putString(Constants.LANG_PARAMS_PREFERENCES.value, lang).apply()
    }
}