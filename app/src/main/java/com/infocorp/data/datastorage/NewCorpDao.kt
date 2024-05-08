package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.NewCorporationsDto

@Dao
interface NewCorpDao {
    @Query("SELECT * FROM newCorpsTable")
    fun loadAllNewCorps(): List<NewCorporationsDto>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInNewCorps(corpId: NewCorporationsDto)
//
//    @Delete
//    fun removeCorpInnFavourite(favouriteId: FavouriteCorporationsDto)
//
//    @Query("SELECT * FROM corporationsTable " +
//            "INNER JOIN favouriteTable ON favouriteTable.id = corporationsTable.id ORDER BY name")
//    fun downloadAllFavouriteCorporations(): LiveData<List<CorporationDto>>
}