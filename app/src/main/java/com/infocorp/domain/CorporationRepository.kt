package com.infocorp.domain

import androidx.lifecycle.LiveData
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.domain.entity.Corporation

interface CorporationRepository {
    fun downloadDataFromFirebase()
    fun downloadDataFromLocalStorage(): LiveData<List<Corporation>>
    fun changeStateCorp(corporation: Corporation)
    fun addCorpToFavourite(corporation: Corporation)
    fun removeCorpFromFavourite(corporation: Corporation)
}