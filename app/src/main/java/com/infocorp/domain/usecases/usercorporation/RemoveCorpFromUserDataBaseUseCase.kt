package com.infocorp.domain.usecases.usercorporation

import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.UserCorporationRepository
import com.infocorp.domain.model.Corporation
import javax.inject.Inject

class RemoveCorpFromUserDataBaseUseCase @Inject constructor(private val repository: UserCorporationRepository) {
    operator fun invoke(corporation: Corporation){
        repository.removeCorpFromUserDataBase(corporation)
    }
}