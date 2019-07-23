package com.sinue.streetworkout.urbandictionary.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import com.sinue.streetworkout.urbandictionary.utils.UtilsCache
import kotlinx.coroutines.*
import retrofit2.HttpException

class UrbanDictionaryRepository {

    private var results = mutableListOf<ItemSearch>()
    private var liveDataResults = MutableLiveData<List<ItemSearch>>()
    val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    var statusSearchLiveData = MutableLiveData <Boolean>()
    private var mutableRequesting: MutableLiveData<Boolean> = MutableLiveData()
    var requesting: LiveData<Boolean> = MutableLiveData()


    private fun thisApiCorService(context: Context?): RestApiService {
        return RestApiService.createCorService(context)
    }


    fun getSearchResults(context: Context?, term: String): MutableLiveData<List<ItemSearch>> {
        //Context can be null, to perform UnitTest, without strategy of cache with OkHttp3

        mutableRequesting.value = true
        requesting = mutableRequesting

        coroutineScope.launch {


            val request = thisApiCorService(context).getSearchList(BuildConfig.API_KEY_URBAN_DIC, term)
            //val request = thisApiCorService(context).getSearchList(BuildConfig.API_KEY_URBAN_DIC, term)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    val mListItemSearch = response
                    if (mListItemSearch != null && mListItemSearch.list != null) {

                        results = mListItemSearch.list as MutableList<ItemSearch>
                        UtilsCache.addToCache(term, mListItemSearch.list)

                        liveDataResults.value = results
                        mutableRequesting.value = false
                        requesting = mutableRequesting

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

        return liveDataResults

    }


}