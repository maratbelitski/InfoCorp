package com.infocorp.di

import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.domain.CorporationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCorporationRepository(impl: CorporationRepositoryImpl): CorporationRepository
}