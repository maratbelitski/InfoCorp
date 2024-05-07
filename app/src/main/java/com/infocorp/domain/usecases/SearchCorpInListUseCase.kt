package com.infocorp.domain.usecases

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.entity.Corporation
import javax.inject.Inject

class SearchCorpInListUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(list: List<Corporation>, text:String): List<Corporation> {
        return repository.searchCorporation(list, text)
    }
}