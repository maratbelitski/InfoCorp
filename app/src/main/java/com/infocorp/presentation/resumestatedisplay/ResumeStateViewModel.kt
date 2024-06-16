package com.infocorp.presentation.resumestatedisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val resumeUseCase: DownloadAllResumeUseCase
) : ViewModel() {

    private var _allResume = MutableStateFlow(emptyList<ResumeState>())
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
}