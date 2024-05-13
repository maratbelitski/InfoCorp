package com.infocorp.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.infocorp.data.datastorage.CorporationDao
import com.infocorp.data.datastorage.CorporationDataBase
import com.infocorp.data.datastorage.FavouriteDao
import com.infocorp.data.datastorage.OldCorpDao
import com.infocorp.data.datastorage.UserCorporationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {


    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): CorporationDataBase{
        return CorporationDataBase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCorporationDao(dataBase: CorporationDataBase): CorporationDao{
        return dataBase.getDaoCorp()
    }

    @Provides
    @Singleton
    fun provideCorporationDaoFavourite(dataBase: CorporationDataBase): FavouriteDao{
        return dataBase.getDaoFavourite()
    }

    @Provides
    @Singleton
    fun provideNewCorporationsDao(dataBase: CorporationDataBase): OldCorpDao{
        return dataBase.getDaoNewCorps()
    }

    @Provides
    @Singleton
    fun provideUserCorporationsDao(dataBase: CorporationDataBase): UserCorporationDao{
        return dataBase.getDaoUserCorp()
    }
}