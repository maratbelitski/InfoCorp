package com.infocorp.di

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
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

//    @Provides
//    @Singleton
//    fun provideReferenceFirebase(firebase: Firebase): DatabaseReference {
//        return firebase.database.getReference(FIRE_BASE_GENERAL)
//    }

    @Provides
    @Singleton
    fun provideRetrofit(): CorporationFactory {
        return CorporationFactory()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(retrofitFactory:CorporationFactory): CorporationService {
        return retrofitFactory.corporationService
    }
}