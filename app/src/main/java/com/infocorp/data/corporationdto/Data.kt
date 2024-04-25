package com.infocorp.data.corporationdto

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("actuality_date") val actuality_date: String,
    @SerializedName("address") val address: String,
    @SerializedName("fio_ru") val fio_ru: Any,

    @SerializedName("full_name_ru") val full_name_ru: String,
    @SerializedName("oked") val oked: String,
    @SerializedName("oked_name") val oked_name: String,
    @SerializedName("registration_date") val registration_date: String,
    @SerializedName("removal_date") val removal_date: Any,

    @SerializedName("short_name_ru") val short_name_ru: String,
    @SerializedName("status") val status: String,
 
    @SerializedName("trade_name_ru") val trade_name_ru: String,
    @SerializedName("type") val type: String,
    @SerializedName("unp") val unp: String
)