package com.infocorp.presentation.favouritedisplay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.usecases.corporation.ChangeStateCorporationToFavouriteUseCase
import com.infocorp.domain.usecases.corporation.DownloadFavouriteFromLocalStorageUseCase
import com.infocorp.domain.usecases.corporation.RemoveCorpFromFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteFragmentViewModel @Inject constructor(
    private val downloadFavourite: DownloadFavouriteFromLocalStorageUseCase,
    private val removeCorpFromFavourite: RemoveCorpFromFavouriteUseCase,
    private val changeStateCorporationToFavourite: ChangeStateCorporationToFavouriteUseCase,
) : ViewModel() {

    val listFavouriteCorp by lazy {
        downloadFavourite.invoke()
    }

    private var _disableBottomNavigation = MutableLiveData(false)
    val disableBottomNavigation: LiveData<Boolean>
        get() = _disableBottomNavigation

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