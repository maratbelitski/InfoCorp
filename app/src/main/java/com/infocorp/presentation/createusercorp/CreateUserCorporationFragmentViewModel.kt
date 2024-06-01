package com.infocorp.presentation.createusercorp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.domain.usecases.usercorporation.AddCorpToUserDtaBaseUseCase
import com.infocorp.domain.usecases.usercorporation.SendUserCorporationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserCorporationFragmentViewModel @Inject constructor(
    private val sendUserCorporation: SendUserCorporationUseCase,
    private val addUserCorporation: AddCorpToUserDtaBaseUseCase,
) : ViewModel() {

    fun sendUserCorporation(userCorp: UserCorporationDto) {
        viewModelScope.launch(Dispatchers.IO) {
            sendUserCorporation.invoke(userCorp)
        }
    }

    fun addUserCorporationToDataBase(userCorp: UserCorporationDto) {
        viewModelScope.launch(Dispatchers.IO) {
            addUserCorporation.invoke(userCorp)
        }
    }

    fun validationError(editText: String, inputLayout: TextInputLayout) {
        val unpText = editText.trim()
        inputLayout.isErrorEnabled = unpText.isEmpty()
    }
}