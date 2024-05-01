package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.CorporationDto

@Dao
interface CorporationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneCorpInDataBase(corporation: CorporationDto)

    @Query("SELECT * FROM corporationsTable")
    fun downloadAllCorporations(): LiveData<List<CorporationDto>>

    @Query("UPDATE corporationsTable SET isFavourite=:isFavourite WHERE id =:id")
    fun updateFavorite(id: String, isFavourite: Boolean)
}