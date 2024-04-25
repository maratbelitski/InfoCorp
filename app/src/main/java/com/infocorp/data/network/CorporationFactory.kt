package com.infocorp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CorporationFactory {
    companion object {
        private const val BASE_URL = "http://suggestions.dadata.ru/suggestions/api/4_1/rs/"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val corporationService = retrofit.create(CorporationService::class.java)
}