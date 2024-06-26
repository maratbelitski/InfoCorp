package com.infocorp.data.corporationdto

import com.google.gson.annotations.SerializedName

data class ServerResponse(
    @SerializedName("suggestions") val suggestionDto: List<SuggestionDto>
)