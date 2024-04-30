package com.infocorp.domain.usecases

import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.entity.Corporation
import javax.inject.Inject

class AddCorpToFavourite @Inject constructor(private val repository: CorporationRepository) {
    operator fun invoke(corporation: Corporation){
        repository.addCorpToFavourite(corporation)
    }
}