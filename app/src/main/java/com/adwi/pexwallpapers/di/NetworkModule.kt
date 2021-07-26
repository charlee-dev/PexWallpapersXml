package com.adwi.pexwallpapers.di

import com.adwi.pexwallpapers.data.remote.PexApi
import com.adwi.pexwallpapers.util.Constants.Companion.API_KEY
import com.adwi.pexwallpapers.util.Constants.Companion.AUTHORIZATION
import com.adwi.pexwallpapers.util.Constants.Companion.BASE_URL
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor { message ->
            Timber.d(message)
        }
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Singleton
    @Provides
    fun provideNetworkInterceptor(): Interceptor =
        Interceptor {
            val request = it.request().newBuilder()
                .addHeader(AUTHORIZATION, API_KEY)
                .build()
            it.proceed(request)
        }

    @Singleton
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): PexApi =
        retrofit.create(PexApi::class.java)
}