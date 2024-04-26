package com.infocorp.data.corporationdto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class CorporationDto(
    val id: String = UUID.randomUUID().toString(),
    val idFirebase: String = "Unknown",
    val name: String = "Unknown",
    val poster: String = "Unknown",
    val description: String = "Unknown",
    val address: String = "Unknown",
    val phones: String = "Unknown",
    val email: String = "Unknown",
    val website: String = "Unknown"
) : Parcelable