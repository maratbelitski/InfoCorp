package com.infocorp.presentation.egrdisplay

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Data
import com.infocorp.domain.usecases.GetInfoEgrByTitleUseCase
import com.infocorp.domain.usecases.GetInfoEgrByUnpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class EgrViewModel @Inject constructor(
    private val getInfoByTitle: GetInfoEgrByTitleUseCase,
    private val getInfoByUnp: GetInfoEgrByUnpUseCase
) : ViewModel() {
    companion object{
        private const val MY_LOG = "MyLog"
        private const val NETWORK_EXCEPTION = "Error. Check your network"
    }

    private var _showShimmer = MutableLiveData(false)
    val showShimmer: LiveData<Boolean>
        get() = _showShimmer

    private var _exceptionNetwork = MutableLiveData("")
    val exceptionNetwork: LiveData<String>
        get() = _exceptionNetwork

    private var _listDataEgr = MutableLiveData<List<Data>>()
    val listDataEgr: LiveData<List<Data>>
        get() = _listDataEgr

//    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        throwable.printStackTrace()
//    }

    fun getInfoEgrByName(titleCorp: String) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                _showShimmer.postValue(true)
                _listDataEgr.postValue(getInfoByTitle.invoke(titleCorp))
            } catch (e: UnknownHostException) {
                _exceptionNetwork.postValue(NETWORK_EXCEPTION)
                Log.i(MY_LOG,"$e")
            } finally {
                delay(1000)
                _showShimmer.postValue(false)
            }
        }
    }

    fun getInfoEgrByUnp(unp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _showShimmer.postValue(true)
                _listDataEgr.postValue(getInfoByUnp.invoke(unp))
            } catch (e: UnknownHostException) {
                _exceptionNetwork.postValue(NETWORK_EXCEPTION)
                Log.i(MY_LOG,"$e")
            } finally {
                delay(1000)
                _showShimmer.postValue(false)
            }
        }
    }
}