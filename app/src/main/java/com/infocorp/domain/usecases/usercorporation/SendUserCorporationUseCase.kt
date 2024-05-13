package com.infocorp.domain.usecases.usercorporation

import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.domain.UserCorporationRepository
import javax.inject.Inject

class SendUserCorporationUseCase @Inject constructor(private val repository: UserCorporationRepository) {
    operator fun invoke(userCorp: UserCorporationDto) {
        repository.sendUserCorporation(userCorp)
    }
}