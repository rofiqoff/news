package com.rofiqoff.news.base.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.rofiqoff.news.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkProvider(
    private val url: String,
) {

    fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)

        return httpBuilder.apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(getHttpLoggingInterceptor())
            }
            addInterceptor { chain ->
                val request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "a3d286135e834c6db3843138b1388219")
                    .build()
                chain.proceed(request)
            }
        }.build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d("Interceptor", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

}
