package com.infocorp.presentation.favouritedisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.usecases.ChangeStateCorporationToFavourite
import com.infocorp.domain.usecases.DownloadFavouriteFromLocalStorageUseCase
import com.infocorp.domain.usecases.RemoveCorpFromFavourite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteFragmentViewModel @Inject constructor(
    private val downloadFavourite: DownloadFavouriteFromLocalStorageUseCase,
    private val removeCorpFromFavourite: RemoveCorpFromFavourite,
    private val changeStateCorporationToFavourite: ChangeStateCorporationToFavourite,
) : ViewModel() {

    val listFavouriteCorp = downloadFavourite.invoke()

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
}