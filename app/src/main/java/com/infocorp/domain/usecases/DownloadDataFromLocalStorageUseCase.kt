package com.infocorp.domain.usecases

import androidx.lifecycle.LiveData
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.domain.Corporation
import com.infocorp.domain.CorporationRepository
import javax.inject.Inject

class DownloadDataFromLocalStorageUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(): LiveData<List<Corporation>> {
      return repository.downloadDataFromLocalStorage()
    }
}