package com.infocorp.data.datastorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.OldCorporationsDto

@Dao
interface OldCorpDao {
    @Query("SELECT * FROM oldCorpsTable")
    fun loadAllOldCorps(): List<OldCorporationsDto>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInOldCorps(corpId: OldCorporationsDto)
//
//    @Delete
//    fun removeCorpInnFavourite(favouriteId: FavouriteCorporationsDto)
//
//    @Query("SELECT * FROM corporationsTable " +
//            "INNER JOIN favouriteTable ON favouriteTable.id = corporationsTable.id ORDER BY name")
//    fun downloadAllFavouriteCorporations(): LiveData<List<CorporationDto>>
}