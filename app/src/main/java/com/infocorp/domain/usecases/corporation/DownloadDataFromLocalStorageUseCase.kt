package com.infocorp.domain.usecases.corporation

import androidx.lifecycle.LiveData
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class DownloadDataFromLocalStorageUseCase @Inject constructor(private val repository: CorporationRepository) {
   operator fun invoke(): LiveData<List<Corporation>> {
        return repository.downloadDataFromLocalStorage()
    }
}