package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.UserCorporationDto

@Dao
interface UserCorporationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneCorpInDataBase(corporation: UserCorporationDto)

    @Query("SELECT * FROM userCorporationsTable ORDER BY name")
    fun downloadAllUserCorporations(): LiveData<List<UserCorporationDto>>

    @Delete
    fun removeCorpFromDataBase(corporation: UserCorporationDto)
}