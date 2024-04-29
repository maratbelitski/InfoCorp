package com.infocorp.presentation.listdisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.usecases.DownloadDataFromFirebaseUseCase
import com.infocorp.domain.usecases.DownloadDataFromLocalStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCorporationsViewModel @Inject constructor(
    private val downloadDataFromFirebase: DownloadDataFromFirebaseUseCase,
    private val downloadDataFromLocalStorage: DownloadDataFromLocalStorageUseCase
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
}