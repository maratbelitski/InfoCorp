package com.infocorp.data.network

import com.infocorp.data.corporationdto.ServerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CorporationService {

    @Headers("Authorization: Token 6ebaf8e334bac982c60b1ba2fae777febca0303f")
    @GET("suggest/party_by")
   suspend fun getAllCorporations(@Query("query") value:String): Response<ServerResponse>
}