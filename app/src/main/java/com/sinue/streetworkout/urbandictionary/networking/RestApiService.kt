package com.sinue.streetworkout.urbandictionary.networking

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ListItemsSearch
import com.sinue.streetworkout.urbandictionary.utils.UtilsNetwork
import kotlinx.coroutines.Deferred
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit

interface RestApiService {

    @Headers("content-type: String/JSON",
        "X-RapidAPI-Host: mashape-community-urban-dictionary.p.rapidapi.com",
        "Cache-Control: max-age=640000")
    @GET("/define")
    fun getSearchList(@Header("X-RapidAPI-Key") apiKey: String,
                      @Query("term") term : String) : Deferred<Response<ListItemsSearch>>

    companion object {

        fun createCorService(context: Context? = null) : RestApiService {

            var okHttpClient: OkHttpClient

            if (context != null) {

                val cacheSize = 10 * 1024 * 1024 // 10 MB
                val httpCacheDirectory = File(context.cacheDir, "http-cache")
                val cache = Cache(httpCacheDirectory, cacheSize.toLong())

                val networkCacheInterceptor = Interceptor { chain ->
                    var request = chain.request()

                    var cacheControl = CacheControl.Builder()
                        .maxAge(5, TimeUnit.MINUTES)
                        .build()

                    if (UtilsNetwork.hasNetwork(context)!!) {
                        request = request.newBuilder()
                            .header("Cache-Control", cacheControl.toString())
                            .removeHeader("Pragma")
                            .build()
                    } else {
                        request.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 1)
                            .build()
                    }

                    chain.proceed(request)
                }

                //Looks amazing, but has its drawbacks
                okHttpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(networkCacheInterceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
            }
            else {

                okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
            }

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