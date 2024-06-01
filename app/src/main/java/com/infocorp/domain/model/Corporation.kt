package com.infocorp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Corporation(
    val id: String = "",
    val idFirebase: String = "",
    var isFavourite: Boolean = false,
    var isNew: Boolean = true,
    val name: String = "",
    val poster: String = "",
    val description: String = "",
    val address: String = "",
    val phones: String = "",
    val email: String = "",
    val website: String = ""
) : Parcelable