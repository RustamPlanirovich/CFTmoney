package com.nauka.cftmoney.di

import android.content.Context
import androidx.room.Room
import com.nauka.cftmoney.ApiInterface
import com.nauka.cftmoney.model.room.AppDatabase
import com.nauka.cftmoney.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Module() {


    companion object {
        @Provides
        fun provideUrl(): String {
            return Constants.BASE_URL
        }

        @Provides
        @Singleton
        fun provideRetrofit(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(provideUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }

        @Provides
        @Singleton
        fun provideRoom(@ApplicationContext applicationContext: Context): AppDatabase {
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "main"
            ).build()
            return db
        }
    }
}