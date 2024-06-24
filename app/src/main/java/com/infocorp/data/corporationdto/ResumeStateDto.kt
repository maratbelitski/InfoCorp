package com.infocorp.data.corporationdto

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infocorp.domain.model.Corporation
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "resumeStateTable")
data class ResumeStateDto(

    val id: String = UUID.randomUUID().toString(),
    @PrimaryKey
    val idCorporation: String = "",
    val poster: String ="",
    val title: String = "",
    val dateSent: String = "",
    val dateResponse: String = "",
    val result: Int = 0,
    val notes: String = "",


    @Embedded(prefix = "resume_")
    val corporation:Corporation
) : Parcelable
