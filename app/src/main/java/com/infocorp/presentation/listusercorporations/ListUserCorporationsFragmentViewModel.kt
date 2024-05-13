package com.infocorp.presentation.listusercorporations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infocorp.domain.usecases.corporation.DownloadDataFromLocalStorageUseCase
import com.infocorp.domain.usecases.usercorporation.DownloadDataFromDataBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListUserCorporationsFragmentViewModel @Inject constructor(
    private val downloadDataFromDatabase: DownloadDataFromDataBaseUseCase
):ViewModel() {
    val listFromLocalSource by lazy {
        downloadDataFromDatabase.invoke()
    }

    private var _disableBottomNavigation = MutableLiveData(true)
    val disableBottomNavigation: LiveData<Boolean>
        get() = _disableBottomNavigation
}