package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Data
import javax.inject.Inject

class GetInfoEgrByUnpUseCase @Inject constructor(private val repository: CorporationRepository) {
    suspend operator fun invoke(unp: String): List<Data> {
        return repository.getInfoEgrByUnp(unp)
    }
}