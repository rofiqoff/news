package com.rofiqoff.news.base.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkProvider(): NetworkProvider {
        return NetworkProvider(
            NetworkUtils.getBaseUrl(),
            NetworkUtils.getApiKey()
        )
    }

    @Singleton
    @Provides
    fun provideRetrofit(networkProvider: NetworkProvider): Retrofit {
        return networkProvider.createRetrofit()
    }

}
