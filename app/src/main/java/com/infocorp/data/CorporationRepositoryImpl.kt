package com.infocorp.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.domain.Corporation
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.usecases.DownloadDataFromFirebaseUseCase
import com.infocorp.presentation.listdisplay.ListCorporationsViewModel
import javax.inject.Inject

class CorporationRepositoryImpl @Inject constructor(
    private val mapper: CorporationMapper,
    private val firebaseReference: DatabaseReference
) : CorporationRepository {

    //download data from Firebase
    override fun downloadDataFromFirebase(){

        val listCorporation = mutableListOf<Corporation>()

        firebaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val responseFirebase = snapshot.children

                responseFirebase.forEach { child ->
                    val corpDto = child.getValue(CorporationDto::class.java)

                    if (corpDto != null) {
                        val corp = mapper.corporationDtoToCorporation(corpDto)
                        val corpWithChildId = corp.copy(id = child.key.toString())
                        Log.i("MyLog", "1 - $corpWithChildId")
                        Log.i("MyLog", "2.1 - ${listCorporation.size}")
                        listCorporation.add(corpWithChildId)
                    }
                }
                listCorporation.clear()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MyLog", error.message)
            }
        })
    }
}