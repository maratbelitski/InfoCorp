package com.infocorp.domain.usecases

import androidx.lifecycle.LiveData
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.entity.Corporation
import javax.inject.Inject

class DownloadDataFromLocalStorageUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(): LiveData<List<Corporation>> {
        return repository.downloadDataFromLocalStorage()
    }
}