package com.infocorp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.datastorage.CorporationDao
import com.infocorp.data.datastorage.FavouriteDao
import com.infocorp.data.datastorage.NewCorpDao
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.model.Corporation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CorporationRepositoryImpl @Inject constructor(
    private val mapper: CorporationMapper,
    private val firebaseReference: DatabaseReference,
    private val daoCorp: CorporationDao,
    private val daoFavourite: FavouriteDao,
    private val daoNewCorps: NewCorpDao,
) : CorporationRepository {


    fun insertDataInLocalDataBase(corpDto: CorporationDto) {
        daoCorp.addOneCorpInDataBase(corpDto)
    }

    private fun giveIdOfFavourite(): List<String> {
        val value = daoFavourite.loadAllFavorite()
        val listId = mutableListOf<String>()

        if (value.isNotEmpty()) value.forEach { listId.add(it.id) }
        return listId
    }

    private fun giveIdOfNewCorps(): List<String> {
        val value = daoNewCorps.loadAllNewCorps()
        val listId = mutableListOf<String>()

        if (value.isNotEmpty()) value.forEach { listId.add(it.id) }
        return listId
    }

    override fun downloadDataFromFirebase() {
        val listIdFavourite = giveIdOfFavourite()
        val listIdNewCorps = giveIdOfNewCorps()

        firebaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val myScope = CoroutineScope(Dispatchers.IO)

                val responseFirebase = snapshot.children

                responseFirebase.forEach { child ->
                    val corpDto = child.getValue(CorporationDto::class.java)
                    val corpDtoWithChildId = corpDto?.copy(id = child.key.toString())

                    if (corpDtoWithChildId != null) {
                        myScope.launch {

                            for (value in listIdFavourite) {
                                if (value == corpDtoWithChildId.id) {
                                    corpDtoWithChildId.isFavourite = true
                                }
                            }

                            for (value in listIdNewCorps) {
                                if (value == corpDtoWithChildId.id) {
                                    corpDtoWithChildId.isNew = false
                                }
                            }
                            insertDataInLocalDataBase(corpDtoWithChildId)
                        }
                    }
                }
                myScope.cancel()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MyLog","Error message ${error.message}" )
            }
        })
    }

    override fun downloadDataFromLocalStorage(): LiveData<List<Corporation>> {
        val dataFromDB = daoCorp.downloadAllCorporations()
        return dataFromDB.map { dto -> dto.map { mapper.corporationDtoToCorporation(it) } }
    }

    override fun downloadFavouriteFromLocalStorage(): LiveData<List<Corporation>> {
        val dataFromDB = daoFavourite.downloadAllFavouriteCorporations()
        return dataFromDB.map { dto -> dto.map { mapper.corporationDtoToCorporation(it) } }
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

    override fun addCorpToFNewCorpsList(corporation: Corporation) {
        val newCorp = mapper.corporationToNewCorp(corporation)
        daoNewCorps.addInNewCorps(newCorp)
    }

    override fun removeCorpFromFavourite(corporation: Corporation) {
        val corpDto = mapper.corporationToCorporationDto(corporation)
        val favouriteCorp = mapper.corporationDtoToFavouriteCorp(corpDto)

        daoFavourite.removeCorpInnFavourite(favouriteCorp)
    }

    override fun searchCorporation(list: List<Corporation>, text:String): List<Corporation> {
        val newListFiltered = list.filter { product ->
            product.name.contains(text.trim(), ignoreCase = true)
        }
        return newListFiltered
    }
}