package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.OldCorporationsDto
import kotlinx.coroutines.flow.Flow

@Dao
interface OldCorpDao {
    @Query("SELECT * FROM oldCorpsTable")
    fun loadAllOldCorps(): List<OldCorporationsDto>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInOldCorps(corpId: OldCorporationsDto)
    @Query("SELECT COUNT(id) FROM oldCorpsTable")
    fun getRowCountOld(): Flow<Int>
}