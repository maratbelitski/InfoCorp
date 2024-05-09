package com.infocorp.presentation.egrdisplay

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.model.Data
import com.infocorp.domain.usecases.GetInfoEgrByTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EgrViewModel @Inject constructor(
    private val getInfoByTitle: GetInfoEgrByTitleUseCase
): ViewModel() {

    private var _listDataEgr = MutableLiveData<List<Data>>()
    val listDataEgr: LiveData<List<Data>>
        get() = _listDataEgr

    fun getInfoEgrByName(titleCorp:String){
        viewModelScope.launch(Dispatchers.IO) {
           _listDataEgr.postValue(getInfoByTitle.invoke(titleCorp))
        }
    }
}