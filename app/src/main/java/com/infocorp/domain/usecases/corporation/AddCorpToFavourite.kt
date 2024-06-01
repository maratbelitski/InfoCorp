package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class AddCorpToFavourite @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(corporation: Corporation) {
        repository.addCorpToFavourite(corporation)
    }
}