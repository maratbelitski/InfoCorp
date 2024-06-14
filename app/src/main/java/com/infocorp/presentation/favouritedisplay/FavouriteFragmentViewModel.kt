package com.infocorp.presentation.favouritedisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.usecases.corporation.ChangeStateCorporationToFavouriteUseCase
import com.infocorp.domain.usecases.corporation.DownloadFavouriteFromLocalStorageUseCase
import com.infocorp.domain.usecases.corporation.RemoveCorpFromFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteFragmentViewModel @Inject constructor(
    private val downloadFavourite: DownloadFavouriteFromLocalStorageUseCase,
    private val removeCorpFromFavourite: RemoveCorpFromFavouriteUseCase,
    private val changeStateCorporationToFavourite: ChangeStateCorporationToFavouriteUseCase,
) : ViewModel() {

    private var _listFavouriteCorp = MutableStateFlow<List<Corporation>>(emptyList())
    val listFavouriteCorp: StateFlow<List<Corporation>>
        get() = _listFavouriteCorp

    init {
        downloadData()
    }

    fun removeCorpFromFavourite(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            removeCorpFromFavourite.invoke(corporation)
        }
    }

    fun changeStateCorp(corporation: Corporation) {
        viewModelScope.launch(Dispatchers.IO) {
            changeStateCorporationToFavourite.invoke(corporation)
        }
    }

    private fun downloadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _listFavouriteCorp.emitAll(downloadFavourite.invoke())
        }
    }
}