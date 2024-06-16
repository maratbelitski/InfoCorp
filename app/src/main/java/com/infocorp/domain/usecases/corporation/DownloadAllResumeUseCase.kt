package com.infocorp.domain.usecases.corporation

import com.infocorp.domain.CorporationRepository
import javax.inject.Inject

class DownloadAllResumeUseCase @Inject constructor(private val repository: CorporationRepository) {
operator fun invoke() = repository.downloadAllResume()
}