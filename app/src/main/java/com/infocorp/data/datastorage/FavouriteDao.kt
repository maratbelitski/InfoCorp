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

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favouriteTable")
    fun loadAllFavorite(): List<FavouriteCorporationsDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInFavourite(favouriteId: FavouriteCorporationsDto)

    @Delete
    fun removeCorpInnFavourite(favouriteId: FavouriteCorporationsDto)

    @Query("SELECT * FROM corporationsTable " +
            "INNER JOIN favouriteTable ON favouriteTable.id = corporationsTable.id ORDER BY name")
    fun downloadAllFavouriteCorporations(): LiveData<List<CorporationDto>>
}