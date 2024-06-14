package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import javax.inject.Inject

class RegistrationUserUseCase @Inject constructor(private val repository: CorporationRepository){
    operator fun invoke(email:String, password:String): Pair<String, String>{
       return repository.registrationUser(email, password)
    }
}