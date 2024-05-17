package com.infocorp.domain.usecases.usercorporation

import androidx.lifecycle.LiveData
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.UserCorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class DownloadDataFromDataBaseUseCase @Inject constructor(private val repository: UserCorporationRepository) {
   operator fun invoke(): LiveData<List<Corporation>> {
        return repository.downloadAllCorporations()
    }
}