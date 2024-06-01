package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Data
import javax.inject.Inject

class GetInfoEgrByTitleUseCase @Inject constructor(private val repository: CorporationRepository) {
    suspend operator fun invoke(titleCorp: String): List<Data> {
        return repository.getInfoEgrByTitle(titleCorp)
    }
}