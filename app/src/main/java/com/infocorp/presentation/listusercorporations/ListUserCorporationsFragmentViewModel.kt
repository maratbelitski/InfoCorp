package com.infocorp.presentation.listusercorporations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.usecases.corporation.DownloadDataFromLocalStorageUseCase
import com.infocorp.domain.usecases.usercorporation.DownloadDataFromDataBaseUseCase
import com.infocorp.domain.usecases.usercorporation.RemoveCorpFromUserDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListUserCorporationsFragmentViewModel @Inject constructor(
    private val downloadDataFromDatabase: DownloadDataFromDataBaseUseCase,
    private val removeCorpFromUserDataBase: RemoveCorpFromUserDataBaseUseCase
):ViewModel() {
    val listFromLocalSource by lazy {
        downloadDataFromDatabase.invoke()
    }

    private var _disableBottomNavigation = MutableLiveData(true)
    val disableBottomNavigation: LiveData<Boolean>
        get() = _disableBottomNavigation

    fun removeCorpFromDataBase(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            removeCorpFromUserDataBase.invoke(corporation)
        }
    }
}