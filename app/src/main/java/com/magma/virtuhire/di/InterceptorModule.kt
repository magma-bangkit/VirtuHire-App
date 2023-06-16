package com.magma.virtuhire.di

import com.magma.virtuhire.data.local.UserPreferences
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
class InterceptorModule {

    @Provides
    @Singleton
    fun provideTokenInterceptor(userPreferences: UserPreferences): TokenInterceptor {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.6/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        return TokenInterceptor(userPreferences, retrofit)
    }
}