package com.sinue.streetworkout.urbandictionary.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ListItemsSearch
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RestApiService {

    @Headers("content-type: String/JSON",
        "X-RapidAPI-Host: mashape-community-urban-dictionary.p.rapidapi.com",
        "Cache-Control: max-age=640000")
    @GET("/define")
    fun getSearchList(@Header("X-RapidAPI-Key") apiKey: String,
                      @Query("term") term : String) : Deferred<ListItemsSearch>

    companion object {

        fun createCorService() : RestApiService {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_URBAN_DIC)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(RestApiService::class.java)
        }
    }
}