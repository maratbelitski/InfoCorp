package com.infocorp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.util.foreignKeyCheck
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.datastorage.CorporationDao
import com.infocorp.data.datastorage.FavouriteDao
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.entity.Corporation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class CorporationRepositoryImpl @Inject constructor(
    private val mapper: CorporationMapper,
    private val firebaseReference: DatabaseReference,
    private val daoCorp: CorporationDao,
    private val daoFavourite: FavouriteDao,
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

    override fun downloadDataFromFirebase() {
        val listIdFavourite = giveIdOfFavourite()

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
                            insertDataInLocalDataBase(corpDtoWithChildId)
                        }
                    }
                }
                myScope.cancel()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MyLog", error.message)
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

    override fun addCorpToFavourite(corporation: Corporation) {
        val corpDto = mapper.corporationToCorporationDto(corporation)
        val favouriteCorp = mapper.corporationDtoToFavouriteCorp(corpDto)

        daoFavourite.addInFavourite(favouriteCorp)
    }

    override fun removeCorpFromFavourite(corporation: Corporation) {
        val corpDto = mapper.corporationToCorporationDto(corporation)
        val favouriteCorp = mapper.corporationDtoToFavouriteCorp(corpDto)

        daoFavourite.removeCorpInnFavourite(favouriteCorp)
    }
}