package com.infocorp.data.datastorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.infocorp.data.corporationdto.CorporationDto

@Dao
interface CorporationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneCorpInDataBase(corporation: CorporationDto)
}