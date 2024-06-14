package com.infocorp.data.corporationdto

import com.google.gson.annotations.SerializedName

data class DataDto(
    @SerializedName("full_name_ru") val titleCorp: String? = "",
    @SerializedName("fio_ru") val fioPerson: String? = "",
    @SerializedName("type") val type: String? = "",
    @SerializedName("oked_name") val direction: String? = "",
    @SerializedName("unp") val unp: String? = "",
    @SerializedName("address") val address: String? = "",
    @SerializedName("status") val status: String? = ""
)