package com.sinue.streetworkout.urbandictionary.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import com.sinue.streetworkout.urbandictionary.utils.UtilsCache
import kotlinx.coroutines.*
import retrofit2.HttpException

class UrbanDictionaryRepository {

    private var results = mutableListOf<ItemSearch>()
    private var mutableLiveData = MutableLiveData<List<ItemSearch>>()
    val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    var statusSearchLiveData = MutableLiveData <Boolean>()


    private fun thisApiCorService(context: Context?): RestApiService {
        return RestApiService.createCorService(context)
    }


    fun getMutableLiveData(context: Context?, term: String): MutableLiveData<List<ItemSearch>> {
        //Context can be null, to perform UnitTest, without strategy of cache with OkHttp3

        coroutineScope.launch {

            val request = thisApiCorService(context).getSearchList(BuildConfig.API_KEY_URBAN_DIC, term)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    val mListItemSearch = response
                    if (mListItemSearch != null && mListItemSearch.list != null) {

                        results = mListItemSearch.list as MutableList<ItemSearch>
                        UtilsCache.addToCache(term, mListItemSearch.list)

                        mutableLiveData.value = results

                    }
                } catch (e: HttpException) {
                    //"true" is for "API call error", can be observed outside this repository for any purpose
                    statusSearchLiveData.value = true
                    // Or Log.e exception //

                } catch (e: Throwable) {
                    //"true" is for "API call error", can be observed outside this repository for any purpose
                    statusSearchLiveData.value = true
                    // Or Log.e error //)
                }

            }
        }

        return mutableLiveData

    }


}