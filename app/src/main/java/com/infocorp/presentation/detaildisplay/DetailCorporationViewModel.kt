package com.infocorp.presentation.detaildisplay

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailCorporationViewModel @Inject constructor(
    private val sharedPref: SharedPreferences
): ViewModel() {

private var _disableBottomNavigation = MutableLiveData(true)
val disableBottomNavigation: LiveData<Boolean>
    get() = _disableBottomNavigation

    init {
        sharedPref.edit().putString("titleCV","Резюме Android разработчик. Белицкий").apply()
        sharedPref.edit().putString("bodyCV","Приветствую. Я Android разработчик и хотел бы присоединиться к вашей команде").apply()
    }

    fun getTittleCvUser(titleCV:String): String{
       return sharedPref.getString(titleCV,"") ?:""
    }

    fun getBodyCvUser(bodyCV:String): String{
        return sharedPref.getString(bodyCV,"") ?:""
    }
}

