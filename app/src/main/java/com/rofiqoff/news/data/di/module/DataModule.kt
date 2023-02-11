package com.rofiqoff.news.data.di.module

import com.rofiqoff.news.data.api.repository.DataRepository
import com.rofiqoff.news.data.implementation.remote.api.DataApi
import com.rofiqoff.news.data.implementation.repository.DataRepositoryImpl
import com.rofiqoff.news.utils.dispatcher.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataApi(retrofit: Retrofit): DataApi {
        return retrofit.create(DataApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataRepository(
        api: DataApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): DataRepository {
        return DataRepositoryImpl(api, ioDispatcher)
    }

}
