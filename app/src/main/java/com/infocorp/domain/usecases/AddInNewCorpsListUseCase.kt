package com.infocorp.domain.usecases

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class AddInNewCorpsListUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(corp:Corporation){
        repository.addCorpToFNewCorpsList(corp)
    }
}