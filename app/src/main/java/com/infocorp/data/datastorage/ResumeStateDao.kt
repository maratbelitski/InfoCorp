package com.infocorp.data.datastorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infocorp.data.corporationdto.ResumeStateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeStateDao {
    @Query("SELECT * FROM resumeStateTable")
    fun loadAllResumes(): Flow<List<ResumeStateDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addResumeInDatabase(resume: ResumeStateDto)

    @Query("SELECT COUNT(id) FROM resumeStateTable")
    fun getRowCountResume(): Flow<Int>
}