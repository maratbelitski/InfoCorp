package com.infocorp.domain

import androidx.lifecycle.LiveData
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.domain.model.Corporation

interface UserCorporationRepository {
    fun sendUserCorporation(userCorp:UserCorporationDto)
    fun addUserCorporationToDataBase(userCorp: UserCorporationDto)
    fun removeCorpFromUserDataBase(userCorp: Corporation)
    fun downloadAllCorporations(): LiveData<List<Corporation>>
}