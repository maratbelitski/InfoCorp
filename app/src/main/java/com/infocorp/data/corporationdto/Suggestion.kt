package com.infocorp.data.corporationdto

import com.google.gson.annotations.SerializedName

data class Suggestion(
    @SerializedName("data") val data: Data,
    @SerializedName("value")val value: String
    //    val unrestricted_value: String,
)