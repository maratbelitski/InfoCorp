package com.infocorp.presentation.account

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.infocorp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val firebase: Firebase,
    private val sharedPref: SharedPreferences
) : ViewModel() {

    private val _contentText = MutableStateFlow("")
    val contentText: StateFlow<String>
        get() = _contentText

    private val _headerText = MutableStateFlow("")
    val headerText: StateFlow<String>
        get() = _headerText

    private val _linkText = MutableStateFlow("")
    val linkText: StateFlow<String>
        get() = _linkText

    private val _currentAccount = MutableStateFlow("")
    val currentAccount: StateFlow<String>
        get() = _currentAccount
    init {
        getHeaderText()
        getContentText()
        getLinkText()
        getCurrentAccount()
    }
    private fun getHeaderText(){
        _headerText.value = sharedPref
            .getString(Constants.TITLE_CV.value, Constants.NOT_SPECIFIED.value).toString()
    }

    private fun getContentText(){
        _contentText.value = sharedPref
            .getString(Constants.BODY_CV.value, Constants.NOT_SPECIFIED.value).toString()
    }

    private fun getLinkText(){
        _linkText.value = sharedPref
            .getString(Constants.LINK_CV.value, Constants.NOT_SPECIFIED.value).toString()
    }

    fun createUserCv(header:String, content:String, link:String){
        sharedPref.edit().putString(Constants.TITLE_CV.value,header).apply()
        sharedPref.edit().putString(Constants.BODY_CV.value,content).apply()
        sharedPref.edit().putString(Constants.LINK_CV.value,link).apply()

        getHeaderText()
        getContentText()
        getLinkText()
    }

    fun validationError(editText: String, inputLayout: TextInputLayout) {
        val unpText = editText.trim()
        if (unpText.isEmpty()){
            inputLayout.isErrorEnabled = true
        } else{
            inputLayout.isErrorEnabled = false
        }
    }
    fun getFirebase(): Firebase {
        return firebase
    }

    private fun getCurrentAccount(){
       _currentAccount.value = firebase.auth.currentUser?.email.toString()
    }

    fun logOut(){
        firebase.auth.signOut()
    }
}