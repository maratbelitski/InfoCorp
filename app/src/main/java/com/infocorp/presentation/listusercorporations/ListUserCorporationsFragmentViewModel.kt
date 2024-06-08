package com.infocorp.presentation.listusercorporations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Corporation
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
) : ViewModel() {
    val listFromLocalSource by lazy {
        downloadDataFromDatabase.invoke()
    }

    fun removeCorpFromDataBase(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            removeCorpFromUserDataBase.invoke(corporation)
        }
    }
}