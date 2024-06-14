package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class SearchCorpInListUseCase @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(list: List<Corporation>, text: String): List<Corporation> {
        return repository.searchCorporation(list, text)
    }
}