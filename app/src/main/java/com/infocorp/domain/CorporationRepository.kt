package com.infocorp.domain

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ValueEventListener

interface CorporationRepository {
   fun downloadDataFromFirebase()
}