package com.infocorp.presentation.createusercorp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.data.corporationdto.CorporationDto
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
): ViewModel() {

    private var _disableBottomNavigation = MutableLiveData(true)
    val disableBottomNavigation: LiveData<Boolean>
        get() = _disableBottomNavigation

    fun sendUserCorporation(userCorp:UserCorporationDto){
        viewModelScope.launch(Dispatchers.IO) {
            sendUserCorporation.invoke(userCorp)
        }
    }

    fun addUserCorporationToDataBase(userCorp: UserCorporationDto){
        viewModelScope.launch(Dispatchers.IO) {
            addUserCorporation.invoke(userCorp)
        }
    }
}