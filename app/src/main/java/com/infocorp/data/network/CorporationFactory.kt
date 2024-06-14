package com.infocorp.data.network

import com.infocorp.utils.Constants.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CorporationFactory {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL.value)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val corporationService = retrofit.create(CorporationService::class.java)
}