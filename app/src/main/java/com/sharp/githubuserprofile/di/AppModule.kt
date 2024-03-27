package com.sharp.githubuserprofile.di

import com.sharp.githubuserprofile.BuildConfig
import com.sharp.githubuserprofile.data.local.UserInfoDataSource
import com.sharp.githubuserprofile.data.local.UserInfoDataSourceImpl
import com.sharp.githubuserprofile.data.remote.repository.UserInfoRepository
import com.sharp.githubuserprofile.data.remote.service.ApiService
import com.sharp.githubuserprofile.utils.AppConstants.TIME_OUT
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = when (BuildConfig.DEBUG) {
                true -> HttpLoggingInterceptor.Level.BODY
                false -> HttpLoggingInterceptor.Level.NONE
            }
        }

        val httpClient = OkHttpClient().newBuilder().apply {
            addInterceptor(httpLoggingInterceptor)
        }

        httpClient.apply {
            readTimeout(TIME_OUT, TimeUnit.SECONDS)
        }

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesUserInfoDataSource(apiService: ApiService): UserInfoDataSource {
        return UserInfoDataSourceImpl(apiService)
    }

    @Singleton
    @Provides
    fun providesUserInfoRepository(userInfoDataSource: UserInfoDataSource): UserInfoRepository {
        return UserInfoRepository(userInfoDataSource)
    }
}