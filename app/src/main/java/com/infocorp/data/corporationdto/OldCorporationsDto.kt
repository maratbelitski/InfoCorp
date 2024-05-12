package com.infocorp.data.corporationdto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oldCorpsTable")
data class OldCorporationsDto (
    @PrimaryKey
    val id: String =""
)