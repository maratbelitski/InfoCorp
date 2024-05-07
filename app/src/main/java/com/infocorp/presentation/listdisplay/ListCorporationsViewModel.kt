package com.infocorp.presentation.listdisplay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.entity.Corporation
import com.infocorp.domain.usecases.AddCorpToFavourite
import com.infocorp.domain.usecases.ChangeStateCorporationToFavourite
import com.infocorp.domain.usecases.DownloadDataFromFirebaseUseCase
import com.infocorp.domain.usecases.DownloadDataFromLocalStorageUseCase
import com.infocorp.domain.usecases.RemoveCorpFromFavourite
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
    private val changeStateCorporationToFavourite: ChangeStateCorporationToFavourite,
    private val addCorpToFavourite: AddCorpToFavourite,
    private val removeCorpFromFavourite: RemoveCorpFromFavourite,
    private val searchCorp: SearchCorpInListUseCase

) : ViewModel() {
    val showShimmer = MutableLiveData(true)
    val listFromLocalSource = downloadDataFromLocalStorage.invoke()

    init {
        downloadDataFromRemoteSource()
    }

    fun searchCorporation(listCorp:List<Corporation>, text:String):List<Corporation>{
        return searchCorp.invoke(listCorp, text)
    }
    private fun downloadDataFromRemoteSource() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadDataFromFirebase.invoke()
            delay(1000)
            showShimmer.postValue(false)
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