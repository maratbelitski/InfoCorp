package com.infocorp.presentation.generaldisplay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.data.UserCorporationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralFragmentViewModel @Inject constructor(
    private val repository: CorporationRepositoryImpl,
    private val repositoryUser: UserCorporationRepositoryImpl
) : ViewModel() {

    private var _showShimmer = MutableStateFlow(true)
    val showShimmer: StateFlow<Boolean>
        get() = _showShimmer.asStateFlow()

    private var _allCorporation = MutableStateFlow(0)
    val allCorporation: StateFlow<Int>
        get() = _allCorporation.asStateFlow()

    private var _userCorporation = MutableSharedFlow<Int>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val userCorporation: SharedFlow<Int>
        get() = _userCorporation.asSharedFlow()

    private var _oldCorporation = MutableSharedFlow<Int>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val oldCorporation: SharedFlow<Int>
        get() = _oldCorporation.asSharedFlow()

    init {
        downloadDataFromRemoteSource()
        getRowCountAllCorp()
        getRowCountUserCorp()
        getRowCountOldCorp()

    }

    private fun downloadDataFromRemoteSource() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.downloadDataFromFirebase()
            delay(1500)
            _showShimmer.emit(false)
        }

    }

    private fun getRowCountAllCorp() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultColdFlow = repository.getRowCount()
            _allCorporation.emitAll(resultColdFlow)
        }
    }

    private fun getRowCountUserCorp() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultColdFlow = repositoryUser.getRowCountUser()
            _userCorporation.emitAll(resultColdFlow)
        }
    }

    private fun getRowCountOldCorp() {
        viewModelScope.launch(Dispatchers.IO) {
            val resultOld = repository.getRowCountOld()
            val resultAll = repository.getRowCount()

            combine(resultOld, resultAll) { old, all ->
                _oldCorporation.emit(all - old)
            }.collect()
        }
    }
}