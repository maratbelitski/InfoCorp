package com.infocorp.presentation.detaildisplay

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.infocorp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailCorporationViewModel @Inject constructor(
    private val sharedPref: SharedPreferences
) : ViewModel() {
    fun getTittleCvUser(): String {
        return sharedPref.getString(Constants.TITLE_CV.value, "") ?: ""
    }

    fun getBodyCvUser(): String {
        return sharedPref.getString(Constants.BODY_CV.value, "") ?: ""
    }

    fun getLinkCvUser(): String {
        return sharedPref.getString(Constants.LINK_CV.value, "") ?: ""
    }
}

