package com.infocorp.data.corporationdto

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "UserCorporationsTable")
data class UserCorporationDto(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val idFirebase: String = "",
    var isFavourite: Boolean = false,
    var isNew: Boolean = true,
    val name: String = "",
    val poster: String = "",
    val description: String = "",
    val address: String = "",
    val phones: String = "",
    val email: String = "",
    val website: String = "",
    val notes: String = "",
    val resumeState: Int = 0
) : Parcelable