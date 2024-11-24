package com.himanshu.cryptotrackerapp.di

import com.himanshu.cryptotrackerapp.data.network.ApiService
import com.himanshu.cryptotrackerapp.data.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService): CryptoRepository {
        return CryptoRepository(apiService)
    }
}