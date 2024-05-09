package com.infocorp.domain.model

import com.google.gson.annotations.SerializedName

data class Data(
    val titleCorp: String = "",
    val fioPerson: String = "",
    val type: String = "",
    val direction: String = "",
    val unp: String = "",
    val address: String = "",
    val status: String = ""
)