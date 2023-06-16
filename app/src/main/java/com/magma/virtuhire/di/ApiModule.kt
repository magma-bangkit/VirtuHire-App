package com.magma.virtuhire.di

import com.magma.virtuhire.data.remote.ApiService
import com.magma.virtuhire.data.remote.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(tokenInterceptor: TokenInterceptor): ApiService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS) // Set the connection timeout
            .readTimeout(20, TimeUnit.SECONDS) // Set the read timeout
            .writeTimeout(20, TimeUnit.SECONDS) // Set the write timeout
            .addInterceptor(tokenInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.6/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}