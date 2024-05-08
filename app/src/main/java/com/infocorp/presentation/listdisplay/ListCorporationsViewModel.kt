package com.infocorp.presentation.listdisplay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.usecases.AddCorpToFavourite
import com.infocorp.domain.usecases.AddInNewCorpsListUseCase
import com.infocorp.domain.usecases.ChangeStateCorporationToFavouriteUseCase
import com.infocorp.domain.usecases.ChangeStateCorporationToOldUseCase
import com.infocorp.domain.usecases.DownloadDataFromFirebaseUseCase
import com.infocorp.domain.usecases.DownloadDataFromLocalStorageUseCase
import com.infocorp.domain.usecases.RemoveCorpFromFavouriteUseCase
import com.infocorp.domain.usecases.SearchCorpInListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCorporationsViewModel @Inject constructor(
    private val downloadDataFromFirebase: DownloadDataFromFirebaseUseCase,
    private val downloadDataFromLocalStorage: DownloadDataFromLocalStorageUseCase,
    private val changeStateCorporationToFavouriteUseCase: ChangeStateCorporationToFavouriteUseCase,
    private val changeStateCorporationToOld: ChangeStateCorporationToOldUseCase,
    private val addCorpToFavourite: AddCorpToFavourite,
    private val removeCorpFromFavouriteUseCase: RemoveCorpFromFavouriteUseCase,
    private val searchCorp: SearchCorpInListUseCase,
    private val addToNewCorpList: AddInNewCorpsListUseCase

) : ViewModel() {
    val showShimmer = MutableLiveData(true)
    val listFromLocalSource = downloadDataFromLocalStorage.invoke()

    init {
        downloadDataFromRemoteSource()
    }

    fun addInNewCorps(corp: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            addToNewCorpList.invoke(corp)
        }
    }

    fun searchCorporation(listCorp: List<Corporation>, text: String): List<Corporation> {
        return searchCorp.invoke(listCorp, text)
    }

    private fun downloadDataFromRemoteSource() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadDataFromFirebase.invoke()
            delay(1000)
            showShimmer.postValue(false)
        }
    }

    fun changeStateFavoriteCorp(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            changeStateCorporationToFavouriteUseCase.invoke(corporation)
        }
    }

    fun changeStateNewCorp(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            changeStateCorporationToOld.invoke(corporation)
        }
    }

    fun addCorpToFavourite(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            addCorpToFavourite.invoke(corporation)
        }
    }

    fun removeCorpFromFavourite(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            removeCorpFromFavouriteUseCase.invoke(corporation)
        }
    }
}