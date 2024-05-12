package com.infocorp.domain

import androidx.lifecycle.LiveData
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data

interface CorporationRepository {
    fun downloadDataFromFirebase()
    fun downloadDataFromLocalStorage(): LiveData<List<Corporation>>
    fun downloadFavouriteFromLocalStorage(): LiveData<List<Corporation>>
    fun changeStateCorp(corporation: Corporation)
    fun changeStateCorpToOld(corporation: Corporation)
    fun addCorpToFavourite(corporation: Corporation)
    fun addCorpToOldCorpsList(corporation: Corporation)
    fun removeCorpFromFavourite(corporation: Corporation)
    fun searchCorporation(list: List<Corporation>, text:String): List<Corporation>
    suspend fun getInfoEgrByTitle(titleCorp: String): List<Data>
    suspend fun getInfoEgrByUnp(unp: String): List<Data>
}