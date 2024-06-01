package com.infocorp.data.corporationdto

import com.google.gson.annotations.SerializedName

data class SuggestionDto(
    @SerializedName("data") val dataDto: DataDto,
)