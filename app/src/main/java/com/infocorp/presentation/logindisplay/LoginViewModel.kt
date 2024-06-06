package com.infocorp.presentation.logindisplay

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.infocorp.domain.usecases.corporation.RegistrationUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val registration: RegistrationUserUseCase,
    private val firebase: Firebase
): ViewModel() {

    fun getEmailPassword(email:String, password:String):Pair<String,String>{
       return registration.invoke(email, password)
    }

    fun getFirebase(): Firebase {
        return firebase
    }
}