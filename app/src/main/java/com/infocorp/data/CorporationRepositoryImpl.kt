package com.infocorp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.datastorage.CorporationDao
import com.infocorp.data.datastorage.FavouriteDao
import com.infocorp.data.datastorage.OldCorpDao
import com.infocorp.data.datastorage.ResumeStateDao
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.data.network.CorporationService
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data
import com.infocorp.domain.model.ResumeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CorporationRepositoryImpl @Inject constructor(
    private val mapper: CorporationMapper,
    private val daoCorp: CorporationDao,
    private val daoFavourite: FavouriteDao,
    private val daoOldCorps: OldCorpDao,
    private val daoResume: ResumeStateDao,
    private val retrofitService: CorporationService
) : CorporationRepository {

    fun clearLocalDataBase() {
        daoCorp.clearCorporationsTable()
    }

    fun giveIdOfFavourite(): List<String> {
        val value = daoFavourite.loadAllFavorite()
        val listId = mutableListOf<String>()

        if (value.isNotEmpty()) value.forEach { listId.add(it.id) }
        return listId
    }

    fun giveIdOfOldCorps(): List<String> {
        val value = daoOldCorps.loadAllOldCorps()
        val listId = mutableListOf<String>()

        if (value.isNotEmpty()) value.forEach { listId.add(it.id) }
        return listId
    }

    override fun downloadDataFromLocalStorage(): LiveData<List<Corporation>> {
        val dataFromDB = daoCorp.downloadAllCorporations()
        return dataFromDB.map { dto -> dto.map { mapper.corporationDtoToCorporation(it) } }
    }

    override fun downloadAllResume(): Flow<List<ResumeState>> {
        val listResumeDto = daoResume.loadAllResumes()
        return listResumeDto.map { dto -> dto.map { mapper.resumeStateDtoToResumeState(it) } }
    }

    override fun downloadAllStateResume(state:Int): Flow<Int> {
       return daoResume.loadAllStates(state)
    }

    override fun downloadFavouriteFromLocalStorage(): Flow<List<Corporation>> {
        val dataFromDB = daoFavourite.downloadAllFavouriteCorporations()
        return dataFromDB.map { list -> list.map { mapper.corporationDtoToCorporation(it) } }
    }

    override fun changeStateCorp(corporation: Corporation) {
        val corpDto = mapper.corporationToCorporationDto(corporation)
        daoCorp.updateFavorite(corpDto.id, !corpDto.isFavourite)
    }

    override fun changeStateCorpToOld(corporation: Corporation) {
        val corpDto = mapper.corporationToCorporationDto(corporation)

        daoCorp.updateNew(corpDto.id, false)
    }

    override fun addCorpToFavourite(corporation: Corporation) {
        val corpDto = mapper.corporationToCorporationDto(corporation)
        val favouriteCorp = mapper.corporationDtoToFavouriteCorp(corpDto)

        daoFavourite.addInFavourite(favouriteCorp)
    }

    override fun addCorpToOldCorpsList(corporation: Corporation) {
        val newCorp = mapper.corporationToOldCorp(corporation)
        daoOldCorps.addInOldCorps(newCorp)
    }

    override fun removeCorpFromFavourite(corporation: Corporation) {
        val corpDto = mapper.corporationToCorporationDto(corporation)
        val favouriteCorp = mapper.corporationDtoToFavouriteCorp(corpDto)

        daoFavourite.removeCorpInnFavourite(favouriteCorp)
    }

    override suspend fun removeResumeFromDatabase(resume: ResumeState) {
        val resumeDto = mapper.resumeStateToResumeStateDto(resume)
        daoResume.removeResumeFromDatabase(resumeDto)
    }

    override suspend fun updateResume(
        resume: ResumeState,
        result: Int,
        notes: String,
        dateResponse: String
    ) {
        val resumeDto = mapper.resumeStateToResumeStateDto(resume)
        daoResume.updateResume(resumeDto.id, result, notes, dateResponse)
    }

    override suspend fun updateResumeState(corporation: Corporation, resumeState: Int) {
        val corpDto = mapper.corporationToCorporationDto(corporation)

        daoCorp.updateResumeState(corpDto.id, resumeState)
    }

    override fun searchCorporation(list: List<Corporation>, text: String): List<Corporation> {
        val newListFiltered = list.filter { product ->
            product.name.contains(text.trim(), ignoreCase = true)
        }
        return newListFiltered
    }

    override fun registrationUser(email: String, password: String): Pair<String, String> {
        return email to password
    }

    override suspend fun getInfoEgrByTitle(titleCorp: String): List<Data> {
        val response = retrofitService.getCorporationsByTittle(titleCorp)

        if (response.isSuccessful) {
            val list = response.body()?.suggestionDto ?: emptyList()
            return list.map { sug -> mapper.dataDtoToData(sug.dataDto) }
        } else {
            throw RuntimeException("Exception in fun getInfoEgrByTitle, code: ${response.code()}")
        }
    }

    override suspend fun getInfoEgrByUnp(unp: String): List<Data> {
        val response = retrofitService.getCorporationsByUnp(unp)

        if (response.isSuccessful) {
            val list = response.body()?.suggestionDto ?: emptyList()
            return list.map { sug -> mapper.dataDtoToData(sug.dataDto) }
        } else {
            throw RuntimeException("Exception in fun getInfoEgrByTitle, code: ${response.code()}")
        }
    }

    override suspend fun getRowCount(): Flow<Int> {
        return daoCorp.getRowCount()
    }

    override suspend fun getRowCountOld(): Flow<Int> {
        return daoOldCorps.getRowCountOld()
    }

    override suspend fun getRowCountResume(): Flow<Int> {
        return daoResume.getRowCountResume()
    }

    override suspend fun addResumeToDatabase(resume: ResumeState) {
        val resumeDto = mapper.resumeStateToResumeStateDto(resume)
        daoResume.addResumeInDatabase(resumeDto)
    }

    fun addAllCorpInDataBase(list: List<CorporationDto>) {
        daoCorp.addAllCorpInDataBase(list)
    }
}