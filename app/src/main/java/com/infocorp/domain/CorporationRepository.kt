package com.infocorp.domain

import androidx.lifecycle.LiveData
import com.infocorp.data.corporationdto.ResumeStateDto
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data
import com.infocorp.domain.model.ResumeState
import kotlinx.coroutines.flow.Flow

interface CorporationRepository {
    fun downloadDataFromLocalStorage(): LiveData<List<Corporation>>
    fun downloadAllResume(): Flow<List<ResumeState>>
    fun downloadFavouriteFromLocalStorage(): Flow<List<Corporation>>
    fun changeStateCorp(corporation: Corporation)
    fun changeStateCorpToOld(corporation: Corporation)
    fun addCorpToFavourite(corporation: Corporation)
    fun addCorpToOldCorpsList(corporation: Corporation)
    fun removeCorpFromFavourite(corporation: Corporation)
    suspend fun removeResumeFromDatabase(resume: ResumeState)
    suspend fun updateResume(resume: ResumeState, result:Int, notes: String, dateResponse: String)
    fun searchCorporation(list: List<Corporation>, text: String): List<Corporation>
    fun registrationUser(email: String, password: String): Pair<String, String>
    suspend fun getInfoEgrByTitle(titleCorp: String): List<Data>
    suspend fun getInfoEgrByUnp(unp: String): List<Data>
    suspend fun getRowCount(): Flow<Int>
    suspend fun getRowCountOld(): Flow<Int>
    suspend fun getRowCountResume(): Flow<Int>
    suspend fun addResumeToDatabase(resume: ResumeState)
}