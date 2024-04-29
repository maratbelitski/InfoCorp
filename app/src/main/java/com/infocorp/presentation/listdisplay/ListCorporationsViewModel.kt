package com.infocorp.presentation.listdisplay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.Corporation
import com.infocorp.domain.usecases.DownloadDataFromFirebaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCorporationsViewModel @Inject constructor(
    private val downloadDataFromFirebase: DownloadDataFromFirebaseUseCase,
) : ViewModel() {


    private val _listFromFirebase = MutableLiveData<MutableList<Corporation>>()
    val listFromFirebase: LiveData<List<Corporation>>
        get() = _listFromFirebase.map { list -> list.map { it } }

    fun downloadDataFromRemoteSource() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadDataFromFirebase.invoke()
        }
    }
}