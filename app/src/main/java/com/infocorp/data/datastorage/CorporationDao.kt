package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.CorporationDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CorporationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneCorpInDataBase(corporation: CorporationDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllCorpInDataBase(list: List<CorporationDto>)

    @Query("SELECT * FROM corporationsTable ORDER BY name")
    fun downloadAllCorporations(): LiveData<List<CorporationDto>>

    @Query("SELECT * FROM corporationsTable WHERE id=:idCorporation LIMIT 1")
    fun downloadOneCorporations(idCorporation: String): Flow<CorporationDto?>


    @Query("UPDATE corporationsTable SET isFavourite=:isFavourite WHERE id =:id")
    fun updateFavorite(id: String, isFavourite: Boolean)

    @Query("UPDATE corporationsTable SET isNew=:isNew WHERE id =:id")
    fun updateNew(id: String, isNew: Boolean)

    @Query("DELETE FROM corporationsTable")
    fun clearCorporationsTable()

    @Query("SELECT COUNT(name) FROM corporationsTable")
    fun getRowCount(): Flow<Int>
}