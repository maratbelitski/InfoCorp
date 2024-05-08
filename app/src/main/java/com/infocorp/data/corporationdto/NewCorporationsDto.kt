package com.infocorp.data.corporationdto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newCorpsTable")
data class NewCorporationsDto (
    @PrimaryKey
    val id: String =""
)