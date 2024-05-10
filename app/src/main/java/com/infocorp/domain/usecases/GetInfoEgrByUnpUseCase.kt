package com.infocorp.domain.usecases

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Data
import com.infocorp.domain.model.Suggestion
import javax.inject.Inject

class GetInfoEgrByUnpUseCase @Inject constructor(private val repository: CorporationRepository) {
    suspend operator fun invoke(unp: String): List<Data> {
        return repository.getInfoEgrByUnp(unp)
    }
}