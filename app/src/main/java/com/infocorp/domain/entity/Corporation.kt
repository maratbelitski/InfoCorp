package com.infocorp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Corporation(
    val id: String = "",
    val idFirebase: String = "",
    var isFavourite:Boolean = false,
    val name: String = "",
    val poster: String = "",
    val description: String = "",
    val address: String = "",
    val phones: String = "",
    val email: String = "",
    val website: String = ""
) : Parcelable