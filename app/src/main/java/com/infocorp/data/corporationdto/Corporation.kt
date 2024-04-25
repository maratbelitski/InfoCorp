package com.infocorp.data.corporationdto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Corporation(
    val id: String,
    val name: String,
    val poster: String,
    val description: String,
    val address: String,
    val phones: String,
    val email: String,
    val website: String
) : Parcelable