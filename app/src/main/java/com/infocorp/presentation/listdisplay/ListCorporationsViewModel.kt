package com.infocorp.presentation.listdisplay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.usecases.corporation.AddCorpToFavourite
import com.infocorp.domain.usecases.corporation.AddInOldCorpsListUseCase
import com.infocorp.domain.usecases.corporation.ChangeStateCorporationToFavouriteUseCase
import com.infocorp.domain.usecases.corporation.ChangeStateCorporationToOldUseCase
import com.infocorp.domain.usecases.corporation.DownloadDataFromFirebaseUseCase
import com.infocorp.domain.usecases.corporation.DownloadDataFromLocalStorageUseCase
import com.infocorp.domain.usecases.corporation.RemoveCorpFromFavouriteUseCase
import com.infocorp.domain.usecases.corporation.SearchCorpInListUseCase
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
    private val addToNewCorpList: AddInOldCorpsListUseCase,
    private val repositoryImpl: CorporationRepositoryImpl

) : ViewModel() {
    val showShimmer = MutableLiveData(true)

    val listFromLocalSource by lazy {
        downloadDataFromLocalStorage.invoke()
    }


    init {
        clearCorporationTable()
        downloadDataFromRemoteSource()
    }


    private fun clearCorporationTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryImpl.clearLocalDataBase()
        }
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