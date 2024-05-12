package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import javax.inject.Inject

class DownloadDataFromFirebaseUseCase @Inject constructor(private val repository: CorporationRepository) {
  operator fun invoke(){
        return repository.downloadDataFromFirebase()
    }
}