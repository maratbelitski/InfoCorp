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
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.domain.Corporation
import com.infocorp.domain.CorporationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CorporationRepositoryImpl @Inject constructor(
    private val mapper: CorporationMapper,
    private val firebaseReference: DatabaseReference,
    private val dao: CorporationDao
) : CorporationRepository {


    fun insertDataInLocalDataBase(corpDto: CorporationDto) {
        dao.addOneCorpInDataBase(corpDto)
    }

    override fun downloadDataFromFirebase() {
        val myScope = CoroutineScope(Dispatchers.IO)

        firebaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val responseFirebase = snapshot.children
                responseFirebase.forEach { child ->

                    val corpDto = child.getValue(CorporationDto::class.java)
                    val corpDtoWithChildId = corpDto?.copy(id = child.key.toString())

                    if (corpDtoWithChildId != null) {
                        myScope.launch {
                            insertDataInLocalDataBase(corpDtoWithChildId)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MyLog", error.message)
            }
        })
    }

    override fun downloadDataFromLocalStorage(): LiveData<List<Corporation>> {
        val dataFromDB = dao.downloadAllCorporations()
        return dataFromDB.map { dto -> dto.map { mapper.corporationDtoToCorporation(it) } }
    }
}