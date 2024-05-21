package com.infocorp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.infocorp.data.corporationdto.UserCorporationDto
import com.infocorp.data.datastorage.UserCorporationDao
import com.infocorp.data.mapper.CorporationMapper
import com.infocorp.domain.UserCorporationRepository
import com.infocorp.domain.model.Corporation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserCorporationRepositoryImpl @Inject constructor(
    private val firebase: Firebase,
    private val userDao: UserCorporationDao,
    private val mapper: CorporationMapper
) : UserCorporationRepository {

    companion object {
        private const val FIRE_BASE_USER = "USER_IT_CORPORATION"
    }

    private val firebaseReference by lazy {
        firebase.database.getReference(FIRE_BASE_USER)
    }

    override fun sendUserCorporation(userCorp: UserCorporationDto) {
        firebaseReference.push().setValue(userCorp)
    }

    override fun addUserCorporationToDataBase(userCorp: UserCorporationDto) {
        userDao.addOneCorpInDataBase(userCorp)
    }

    override fun removeCorpFromUserDataBase(userCorp: Corporation) {
        val corpUserDto = mapper.corporationToUserCorporation(userCorp)
        userDao.removeCorpFromDataBase(corpUserDto)
    }

    override fun downloadAllCorporations(): LiveData<List<Corporation>> {
        val dataFromDB = userDao.downloadAllUserCorporations()
        return dataFromDB.map { dto -> dto.map { mapper.userCorporationDtoToCorporation(it) } }
    }

    override suspend fun getRowCountUser(): Flow<Int> {
        return userDao.getRowCountUser()
    }
}