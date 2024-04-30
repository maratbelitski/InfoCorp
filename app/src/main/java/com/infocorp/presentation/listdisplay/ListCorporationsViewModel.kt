package com.infocorp.presentation.listdisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.entity.Corporation
import com.infocorp.domain.usecases.AddCorpToFavourite
import com.infocorp.domain.usecases.ChangeStateCorporationToFavourite
import com.infocorp.domain.usecases.DownloadDataFromFirebaseUseCase
import com.infocorp.domain.usecases.DownloadDataFromLocalStorageUseCase
import com.infocorp.domain.usecases.RemoveCorpFromFavourite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCorporationsViewModel @Inject constructor(
    private val downloadDataFromFirebase: DownloadDataFromFirebaseUseCase,
    private val downloadDataFromLocalStorage: DownloadDataFromLocalStorageUseCase,
    private val changeStateCorporationToFavourite: ChangeStateCorporationToFavourite,
    private val addCorpToFavourite: AddCorpToFavourite,
    private val removeCorpFromFavourite: RemoveCorpFromFavourite

) : ViewModel() {

    init {
        downloadDataFromRemoteSource()
    }

    val listFromFirebase = downloadDataFromLocalStorage.invoke()

    private fun downloadDataFromRemoteSource() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadDataFromFirebase.invoke()
        }
    }

    fun changeStateCorp(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            changeStateCorporationToFavourite.invoke(corporation)
        }
    }

    fun addCorpToFavourite(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            addCorpToFavourite.invoke(corporation)
        }
    }

    fun removeCorpFromFavourite(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            removeCorpFromFavourite.invoke(corporation)
        }
    }
}