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
    @SerializedName("idFirebase") val idFirebase: String = "",
    var isFavourite:Boolean = false,
    @SerializedName("name") val name: String = "",
    @SerializedName("poster") val poster: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("address") val address: String = "",
    @SerializedName("phones") val phones: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("website") val website: String = ""
) : Parcelable