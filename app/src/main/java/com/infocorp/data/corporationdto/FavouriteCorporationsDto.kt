package com.infocorp.data.corporationdto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favouriteTable")
data class FavouriteCorporationsDto (
    @PrimaryKey
    val id: String =""
)