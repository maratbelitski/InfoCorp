package com.infocorp.domain

import androidx.lifecycle.LiveData
import com.infocorp.domain.entity.Corporation

interface CorporationRepository {
    fun downloadDataFromFirebase()
    fun downloadDataFromLocalStorage(): LiveData<List<Corporation>>
    fun addToFavourite(corporation: Corporation)
}