package com.infocorp.domain

import androidx.lifecycle.LiveData
import com.infocorp.domain.model.Corporation

interface CorporationRepository {
    fun downloadDataFromFirebase()
    fun downloadDataFromLocalStorage(): LiveData<List<Corporation>>
    fun downloadFavouriteFromLocalStorage(): LiveData<List<Corporation>>
    fun changeStateCorp(corporation: Corporation)
    fun addCorpToFavourite(corporation: Corporation)
    fun removeCorpFromFavourite(corporation: Corporation)
    fun searchCorporation(list: List<Corporation>, text:String): List<Corporation>
}