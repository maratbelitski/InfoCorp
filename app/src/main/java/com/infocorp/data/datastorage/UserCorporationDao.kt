package com.infocorp.data.datastorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.CorporationDto
import com.infocorp.data.corporationdto.UserCorporationDto
import retrofit2.http.DELETE

@Dao
interface UserCorporationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOneCorpInDataBase(corporation: UserCorporationDto)

    @Query("SELECT * FROM userCorporationsTable ORDER BY name")
    fun downloadAllCorporations(): LiveData<List<UserCorporationDto>>

//    @Query("UPDATE corporationsTable SET isFavourite=:isFavourite WHERE id =:id")
//    fun updateFavorite(id: String, isFavourite: Boolean)
//
//    @Query("UPDATE corporationsTable SET isNew=:isNew WHERE id =:id")
//    fun updateNew(id: String, isNew: Boolean)
//
//    @Query("DELETE FROM corporationsTable")
//    fun clearCorporationsTable()
}