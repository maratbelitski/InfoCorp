package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.UserCorporationDto
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCorporationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneCorpInDataBase(corporation: UserCorporationDto)

    @Query("SELECT * FROM userCorporationsTable ORDER BY name")
    fun downloadAllUserCorporations(): LiveData<List<UserCorporationDto>>

    @Query("SELECT * FROM usercorporationstable WHERE id=:idCorporation LIMIT 1")
    fun downloadOneUserCorporations(idCorporation: String): Flow<UserCorporationDto?>

    @Delete
    fun removeCorpFromDataBase(corporation: UserCorporationDto)

    @Query("SELECT COUNT(name) FROM userCorporationsTable")
    fun getRowCountUser(): Flow<Int>
}