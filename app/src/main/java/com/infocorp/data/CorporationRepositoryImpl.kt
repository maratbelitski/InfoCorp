package com.infocorp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.datastorage.CorporationDao
import com.infocorp.data.datastorage.FavouriteDao
import com.infocorp.data.datastorage.OldCorpDao
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.data.network.CorporationService
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import com.infocorp.domain.model.Data
import com.infocorp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class CorporationRepositoryImpl @Inject constructor(
    private val mapper: CorporationMapper,
  //  private val firebase: Firebase,
    private val daoCorp: CorporationDao,
    private val daoFavourite: FavouriteDao,
    private val daoOldCorps: OldCorpDao,
    private val retrofitService: CorporationService
) : CorporationRepository {

//    private val firebaseReference by lazy {
//        firebase.database.getReference(Constants.GENERAL_DB.value)
//    }

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

    override suspend fun downloadDataFromFirebase() {
//        val listIdFavourite = giveIdOfFavourite()
//        val listIdOldCorps = giveIdOfOldCorps()
//        val listFromFirebase = mutableListOf<CorporationDto>()
//
//        clearLocalDataBase()
//
//        firebaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val myScope = CoroutineScope(Dispatchers.IO)
//                val responseFirebase = snapshot.children
//
//                responseFirebase.forEach { child ->
//                    val corpDto = child.getValue(CorporationDto::class.java)
//                    val corpDtoWithChildId = corpDto?.copy(id = child.key.toString())
//
//                    if (corpDtoWithChildId != null) {
//
//                        for (value in listIdFavourite) {
//                            if (value == corpDtoWithChildId.id) {
//                                corpDtoWithChildId.isFavourite = true
//                                corpDtoWithChildId.isNew = false
//                            }
//                        }
//
//                        for (value in listIdOldCorps) {
//                            if (value == corpDtoWithChildId.id) {
//                                corpDtoWithChildId.isNew = false
//                            }
//                        }
//
//                        listFromFirebase.add(corpDtoWithChildId)
//
//                    }
//                }
//                myScope.launch {
//                    daoCorp.addAllCorpInDataBase(listFromFirebase)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("MyLog", "Error message ${error.message}")
//            }
//        })
    }

    override fun downloadDataFromLocalStorage(): LiveData<List<Corporation>> {
        val dataFromDB = daoCorp.downloadAllCorporations()
        return dataFromDB.map { dto -> dto.map { mapper.corporationDtoToCorporation(it) } }
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

    override fun searchCorporation(list: List<Corporation>, text: String): List<Corporation> {
        val newListFiltered = list.filter { product ->
            product.name.contains(text.trim(), ignoreCase = true)
        }
        return newListFiltered
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

    fun addAllCorpInDataBase(list:List<CorporationDto>) {
        daoCorp.addAllCorpInDataBase(list)
    }
}