package com.infocorp.domain.usecases

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class ChangeStateCorporationToFavourite @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(corporation: Corporation) {
        repository.changeStateCorp(corporation)
    }
}