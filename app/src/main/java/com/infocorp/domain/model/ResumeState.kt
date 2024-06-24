package com.infocorp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ResumeState(
    val id: String = UUID.randomUUID().toString(),
    val idCorporation: String = "",
    val poster: String ="",
    val title: String = "",
    val dateSent: String = "",
    val dateResponse: String = "",
    val result: Int = 0,
    val notes: String = "",
    val corporation:Corporation
) : Parcelable