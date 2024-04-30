package com.infocorp.domain.usecases

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.entity.Corporation
import javax.inject.Inject

class AddCorporationToFavourite @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(corporation: Corporation) {
        repository.addToFavourite(corporation)
    }
}