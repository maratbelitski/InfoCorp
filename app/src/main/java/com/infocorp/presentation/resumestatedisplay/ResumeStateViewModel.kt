package com.infocorp.presentation.resumestatedisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.ResumeState
import com.infocorp.domain.usecases.corporation.DownloadAllResumeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResumeStateViewModel @Inject constructor(
    private val resumeUseCase: DownloadAllResumeUseCase,
    private val repository: CorporationRepository
) : ViewModel() {

    private var _allResume = MutableStateFlow<List<ResumeState>>(emptyList())
    val allResume: StateFlow<List<ResumeState>>
        get() = _allResume.asStateFlow()

    init {
        downloadResumes()
    }
    private fun downloadResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            _allResume.emitAll(resumeUseCase.invoke())
        }
    }

    fun removeResume(resume: ResumeState){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeResumeFromDatabase(resume)
        }
    }

    fun updateResume(resume: ResumeState, result:Int, notes: String, dateResponse: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateResume(resume, result, notes, dateResponse)
        }
    }
}