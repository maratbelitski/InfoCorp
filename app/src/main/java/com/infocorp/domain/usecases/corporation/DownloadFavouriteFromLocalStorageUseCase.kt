package com.infocorp.domain.usecases.corporation

import androidx.lifecycle.LiveData
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadFavouriteFromLocalStorageUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(): Flow<List<Corporation>> {
        return repository.downloadFavouriteFromLocalStorage()
    }
}