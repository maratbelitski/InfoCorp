package com.infocorp.data.corporationdto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@Entity(tableName = "corporationsTable")
data class CorporationDto(
    @PrimaryKey
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("idFirebase") val idFirebase: String = "Unknown",
    var isFavourite:Boolean = false,
    @SerializedName("name") val name: String = "Unknown",
    @SerializedName("poster") val poster: String = "Unknown",
    @SerializedName("description") val description: String = "Unknown",
    @SerializedName("address") val address: String = "Unknown",
    @SerializedName("phones") val phones: String = "Unknown",
    @SerializedName("email") val email: String = "Unknown",
    @SerializedName("website") val website: String = "Unknown"
) : Parcelable