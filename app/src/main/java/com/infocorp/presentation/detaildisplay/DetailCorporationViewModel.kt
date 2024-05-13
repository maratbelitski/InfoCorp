package com.infocorp.presentation.detaildisplay

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailCorporationViewModel @Inject constructor(): ViewModel() {

private var _disableBottomNavigation = MutableLiveData(true)
val disableBottomNavigation: LiveData<Boolean>
    get() = _disableBottomNavigation
}