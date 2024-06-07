package com.infocorp.data.network

import com.infocorp.data.corporationdto.ServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CorporationService {

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
        "Authorization: Token"
    )
    @GET("suggest/party_by")
    suspend fun getCorporationsByTittle(@Query("query") titleCorp: String): Response<ServerResponse>

    @Headers("Authorization: Token ")
    @GET("findById/party_by")
    suspend fun getCorporationsByUnp(@Query("query") unpCorp: String): Response<ServerResponse>
}
