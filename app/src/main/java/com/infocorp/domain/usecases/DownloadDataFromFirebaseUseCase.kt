package com.infocorp.domain.usecases

import androidx.lifecycle.MutableLiveData
import com.infocorp.domain.Corporation
import com.infocorp.domain.CorporationRepository
import javax.inject.Inject

class DownloadDataFromFirebaseUseCase @Inject constructor(private val repository: CorporationRepository) {
   operator fun invoke(){
        return repository.downloadDataFromFirebase()
    }
}