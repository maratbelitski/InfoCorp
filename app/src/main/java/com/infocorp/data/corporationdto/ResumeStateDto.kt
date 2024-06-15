package com.infocorp.data.corporationdto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ResumeStateDto(
    val id: String = UUID.randomUUID().toString(),
    val idCorporation: String = "",
    val poster: String ="",
    val title: String = "",
    val dateSent: String = "",
    val dateResponse: String = "",
    val result: Int = 0,
    val notes: String = ""
) : Parcelable
