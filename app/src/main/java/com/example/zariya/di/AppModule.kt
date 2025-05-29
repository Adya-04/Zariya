package com.example.zariya.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.zariya.pref.PrefDatastore
import com.example.zariya.pref.PrefDatastoreImpl
import com.example.zariya.service.AuthApi
import com.example.zariya.service.HelperApi
import com.example.zariya.service.RecruiterApi
import com.example.zariya.utils.Constants.BASE_URL
import com.example.zariya.utils.Constants.ZARIYA_DATASTORE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
        .readTimeout(30, TimeUnit.SECONDS)     // Read timeout
        .writeTimeout(30, TimeUnit.SECONDS)    // Write timeout
        .build()

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Builder {
        return Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun providesAuthApi(retrofitBuilder: Builder): AuthApi {
        return retrofitBuilder.build().create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesHelperApi(retrofitBuilder: Builder): HelperApi{
        return retrofitBuilder.build().create(HelperApi::class.java)
    }

    @Singleton
    @Provides
    fun providesRecruiterApi(retrofitBuilder: Builder): RecruiterApi{
        return retrofitBuilder.build().create(RecruiterApi::class.java)
    }

    @Singleton
    @Provides
    fun providesDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = {
                    emptyPreferences()
                }
            ),
            produceFile = {
                context.preferencesDataStoreFile(ZARIYA_DATASTORE)
            }
        )
    }

    @Singleton
    @Provides
    fun providesDataPref(datastore: DataStore<Preferences>): PrefDatastore =
        PrefDatastoreImpl(datastore)
}