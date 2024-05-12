package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class AddInOldCorpsListUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(corp:Corporation){
        repository.addCorpToOldCorpsList(corp)
    }
}