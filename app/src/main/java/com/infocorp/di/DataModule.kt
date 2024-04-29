package com.infocorp.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.data.datastorage.CorporationDao
import com.infocorp.data.datastorage.CorporationDataBase
import com.infocorp.domain.CorporationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    companion object {
        private const val FIRE_BASE_KEY = "CORPORATION"
    }

    @Provides
    @Singleton
    fun provideFireBase(): Firebase {
        return Firebase
    }

    @Provides
    @Singleton
    fun provideReferenceFirebase(firebase: Firebase): DatabaseReference {
        return firebase.database.getReference(FIRE_BASE_KEY)
    }

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): CorporationDataBase{
        return CorporationDataBase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCorporationDao(dataBase: CorporationDataBase): CorporationDao{
        return dataBase.getDao()
    }
}