package com.infocorp.presentation.resumestatedisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.UserCorporationRepository
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.ResumeState
import com.infocorp.domain.usecases.corporation.DownloadAllResumeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResumeStateViewModel @Inject constructor(
    private val resumeUseCase: DownloadAllResumeUseCase,
    private val repository: CorporationRepository,
    private val repositoryUser: UserCorporationRepository
) : ViewModel() {

    private var _allResume = MutableStateFlow<List<ResumeState>>(emptyList())
    val allResume: StateFlow<List<ResumeState>>
        get() = _allResume.asStateFlow()

//    private var _corpFromResume = MutableStateFlow(Corporation())
//    val corpFromResume: StateFlow<Corporation>
//        get() = _corpFromResume.asStateFlow()

    private var _shimmer = MutableStateFlow(true)
    val shimmer: StateFlow<Boolean>
        get() = _shimmer.asStateFlow()

    private var _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean>
        get() = _isLoaded.asStateFlow()


    fun downloadResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            val resume = resumeUseCase.invoke()

            resume.collect {
                _allResume.emit(it)

                when (it.isEmpty()) {
                    true -> _isLoaded.emit(true)
                    false -> _isLoaded.emit(false)
                }
                _shimmer.emit(false)
            }
        }
    }

    fun removeResume(resume: ResumeState) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeResumeFromDatabase(resume)
        }
    }

    fun updateResume(resume: ResumeState, result: Int, notes: String, dateResponse: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateResume(resume, result, notes, dateResponse)
        }
    }

//  suspend fun getCorporationFromResume(idCorporation: String) {
//       // viewModelScope.launch(Dispatchers.IO) {
//            val corpFlow = repository.downloadOneCorporation(idCorporation)
//            val corpUserFlow = repositoryUser.downloadOneUserCorporations(idCorporation)
//
//            corpFlow.collect { corp ->
//                if (corp?.name?.isNotEmpty() == true) {
//                    _corpFromResume.emit(corp)
//                } else {
//                    corpUserFlow.collect {
//                        it?.let { _corpFromResume.emit(it) }
//                    }
//                }
//           // }
//        }
//    }
}