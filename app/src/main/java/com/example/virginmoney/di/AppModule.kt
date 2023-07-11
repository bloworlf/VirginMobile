package com.example.virginmoney.di

import com.example.virginmoney.data.repo.Repository
import com.example.virginmoney.data.repo.RepositoryImpl
import com.example.virginmoney.data.network.PeopleCall
import com.example.virginmoney.data.network.RoomsCall
import com.example.virginmoney.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesOkHttpInstance(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun providesRetrofitInstance(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun providesPeople(retrofit: Retrofit): PeopleCall {
        return retrofit.create(PeopleCall::class.java)
    }

    @Provides
    fun providesRooms(retrofit: Retrofit): RoomsCall {
        return retrofit.create(RoomsCall::class.java)
    }

    @Provides
    fun provideRepo(peopleCall: PeopleCall, roomsCall: RoomsCall): Repository {
        return RepositoryImpl(peopleCall, roomsCall)
    }
}