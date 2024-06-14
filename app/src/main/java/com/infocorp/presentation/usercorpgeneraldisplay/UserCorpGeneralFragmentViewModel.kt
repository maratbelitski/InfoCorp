package com.infocorp.presentation.usercorpgeneraldisplay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserCorpGeneralFragmentViewModel @Inject constructor() : ViewModel(){

    private var _disableBottomNavigation = MutableLiveData(true)
    val disableBottomNavigation: LiveData<Boolean>
        get() = _disableBottomNavigation
}