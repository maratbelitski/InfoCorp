package com.infocorp.data.datastorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.FavouriteCorporationsDto
import com.infocorp.data.corporationdto.ResumeStateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeStateDao {
    @Query("SELECT * FROM resumeStateTable")
    fun loadAllResumes(): Flow<List<ResumeStateDto>>

    @Query("SELECT COUNT(id) FROM resumeStateTable WHERE result=:state")
    fun loadAllStates(state:Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addResumeInDatabase(resume: ResumeStateDto)

    @Query("SELECT COUNT(id) FROM resumeStateTable")
    fun getRowCountResume(): Flow<Int>

    @Query("UPDATE resumeStateTable SET result=:result," +
            " notes =:notes," +
            " dateResponse =:dateResponse WHERE id =:id")
    suspend fun updateResume(id: String, result: Int, notes: String, dateResponse: String)

    @Delete
    suspend fun removeResumeFromDatabase(resume: ResumeStateDto)
}