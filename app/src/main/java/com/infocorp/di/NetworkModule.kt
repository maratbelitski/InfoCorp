package com.infocorp.di

import com.google.firebase.Firebase
import com.infocorp.data.network.CorporationFactory
import com.infocorp.data.network.CorporationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideFireBase(): Firebase {
        return Firebase
    }

    @Provides
    @Singleton
    fun provideRetrofit(): CorporationFactory {
        return CorporationFactory()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofitFactory: CorporationFactory): CorporationService {
        return retrofitFactory.corporationService
    }
}