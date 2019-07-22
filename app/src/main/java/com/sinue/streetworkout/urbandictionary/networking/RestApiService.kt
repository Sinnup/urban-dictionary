package com.sinue.streetworkout.urbandictionary.networking

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ListItemsSearch
import com.sinue.streetworkout.urbandictionary.utils.UtilsNetwork
import kotlinx.coroutines.Deferred
import okhttp3.Cache
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

        fun createCorService(context: Context? = null) : RestApiService {

            var okHttpClient: OkHttpClient

            if (context != null) {

                val cacheSize = (5 * 1024 * 1024).toLong()
                val myCache = Cache(context.cacheDir, cacheSize)

                //Looks amazing, but has its drawbacks
                okHttpClient = OkHttpClient.Builder()
                    .cache(myCache)
                    .addInterceptor{chain ->
                        var request = chain.request()
                        request = if (UtilsNetwork.hasNetwork(context)!!)
                        /*
                        *  If there is Internet, get the cache that was stored 5 seconds ago.
                        *  If the cache is older than 5 seconds, then discard it,
                        *  and indicate an error in fetching the response.
                        *  The 'max-age' attribute is responsible for this behavior.
                        */
                            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                        else
                        /*
                        *  If there is no Internet, get the cache that was stored 1 day ago.
                        *  If the cache is older than 1 days, then discard it,
                        *  and indicate an error in fetching the response.
                        *  The 'max-stale' attribute is responsible for this behavior.
                        *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                        */
                            request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 1).build()

                        chain.proceed(request)
                    }
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