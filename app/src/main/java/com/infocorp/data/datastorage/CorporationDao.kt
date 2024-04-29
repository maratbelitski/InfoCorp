package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.domain.Corporation

@Dao
interface CorporationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneCorpInDataBase(corporation: CorporationDto)

    @Query("SELECT * FROM corporations")
    fun downloadAllCorporations(): LiveData<List<CorporationDto>>
}