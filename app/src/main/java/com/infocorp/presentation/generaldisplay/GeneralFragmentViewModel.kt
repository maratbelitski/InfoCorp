package com.infocorp.presentation.generaldisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.data.UserCorporationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralFragmentViewModel @Inject constructor(
    private val repository: CorporationRepositoryImpl,
    private val repositoryUser: UserCorporationRepositoryImpl
) : ViewModel() {

    private var _allCorporation = MutableStateFlow(0)
    val allCorporation: StateFlow<Int>
        get() = _allCorporation.asStateFlow()

    private var _userCorporation = MutableStateFlow(0)
    val userCorporation: StateFlow<Int>
        get() = _userCorporation.asStateFlow()

    private var _oldCorporation = MutableStateFlow(0)
    val oldCorporation: StateFlow<Int>
        get() = _oldCorporation.asStateFlow()

    init {
        downloadDataFromRemoteSource()
        getRowCountAllCorp()
        getRowCountUserCorp()
        getRowCountOldCorp()
    }

    private fun downloadDataFromRemoteSource() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.downloadDataFromFirebase()
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
            val resultAll = repository.getRowCount()
            val resultOld = repository.getRowCountOld()

            combine(resultOld, resultAll) { old, all ->
                if (all != 0) {
                    _oldCorporation.emit(all - old)
                } else {
                    _oldCorporation.emit(0)
                }
            }.collect()
        }
    }
}