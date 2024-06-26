package com.infocorp.presentation.egrdisplay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.infocorp.domain.model.Data
import com.infocorp.domain.usecases.corporation.GetInfoEgrByTitleUseCase
import com.infocorp.domain.usecases.corporation.GetInfoEgrByUnpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class EgrViewModel @Inject constructor(
    private val getInfoByTitle: GetInfoEgrByTitleUseCase,
    private val getInfoByUnp: GetInfoEgrByUnpUseCase
) : ViewModel() {
    companion object{
        private const val NETWORK_EXCEPTION = "Error. Check your network"
    }

    private var _showShimmer = MutableStateFlow(false)
    val showShimmer: StateFlow<Boolean>
        get() = _showShimmer

    private var _exceptionNetwork = MutableLiveData("")
    val exceptionNetwork: LiveData<String>
        get() = _exceptionNetwork

    private var _listDataEgr = MutableLiveData<List<Data>>()
    val listDataEgr: LiveData<List<Data>>
        get() = _listDataEgr

    fun getInfoEgrByName(titleCorp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _showShimmer.emit(true)

            try {
                _listDataEgr.postValue(getInfoByTitle.invoke(titleCorp))
            } catch (e: UnknownHostException) {
                _exceptionNetwork.postValue(NETWORK_EXCEPTION)
                Log.i("MyLog", e.toString())
            } finally {
                _showShimmer.emit(false)
            }
        }
    }

    fun getInfoEgrByUnp(unp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _showShimmer.emit(true)

            try {
                _listDataEgr.postValue(getInfoByUnp.invoke(unp))
            } catch (e: UnknownHostException) {
                _exceptionNetwork.postValue(NETWORK_EXCEPTION)
                Log.i("MyLog", e.toString())
            } finally {
                _showShimmer.emit(false)
            }
        }
    }

    fun validationError(editText: String, inputLayout: TextInputLayout) {
        val unpText = editText.trim()
        inputLayout.isErrorEnabled = unpText.isEmpty()
    }
}