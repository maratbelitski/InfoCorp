package com.infocorp.domain.usecases.resume

import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.ResumeState
import javax.inject.Inject

class AddSubmittedResumeUseCase @Inject constructor(private val repository: CorporationRepository) {
    suspend operator fun invoke(resume: ResumeState) = repository.addResumeToDatabase(resume)
}