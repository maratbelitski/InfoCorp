package com.infocorp.domain.usecases

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class ChangeStateCorporationToOldUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(corporation: Corporation) {
        repository.changeStateCorpToOld(corporation)
    }
}