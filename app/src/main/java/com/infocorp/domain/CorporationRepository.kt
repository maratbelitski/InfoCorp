package com.infocorp.domain

import androidx.lifecycle.LiveData
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data
import kotlinx.coroutines.flow.Flow

interface CorporationRepository {
    fun downloadDataFromLocalStorage(): LiveData<List<Corporation>>
    fun downloadFavouriteFromLocalStorage(): Flow<List<Corporation>>
    fun changeStateCorp(corporation: Corporation)
    fun changeStateCorpToOld(corporation: Corporation)
    fun addCorpToFavourite(corporation: Corporation)
    fun addCorpToOldCorpsList(corporation: Corporation)
    fun removeCorpFromFavourite(corporation: Corporation)
    fun searchCorporation(list: List<Corporation>, text: String): List<Corporation>

    fun registrationUser(email:String, password:String):Pair<String,String>
    suspend fun getInfoEgrByTitle(titleCorp: String): List<Data>
    suspend fun getInfoEgrByUnp(unp: String): List<Data>
    suspend fun getRowCount(): Flow<Int>
    suspend fun getRowCountOld(): Flow<Int>
}