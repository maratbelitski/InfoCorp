package com.infocorp.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ValueEventListener
import com.infocorp.data.corporationdto.CorporationDto

interface CorporationRepository {
   fun downloadDataFromFirebase()
   fun downloadDataFromLocalStorage(): LiveData<List<Corporation>>
}