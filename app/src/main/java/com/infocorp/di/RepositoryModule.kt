package com.infocorp.di

import com.infocorp.data.CorporationRepositoryImpl
import com.infocorp.data.UserCorporationRepositoryImpl
import com.infocorp.domain.CorporationRepository
import com.infocorp.domain.UserCorporationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCorporationRepository(impl: CorporationRepositoryImpl): CorporationRepository
    @Binds
    abstract fun bindUserCorporationRepository(impl: UserCorporationRepositoryImpl): UserCorporationRepository
}