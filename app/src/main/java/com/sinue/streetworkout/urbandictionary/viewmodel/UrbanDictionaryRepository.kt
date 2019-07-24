package com.sinue.streetworkout.urbandictionary.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import com.sinue.streetworkout.urbandictionary.networking.Result
import com.sinue.streetworkout.urbandictionary.utils.UtilsCache
import kotlinx.coroutines.*
import retrofit2.HttpException

class UrbanDictionaryRepository {

    private var mutLiveDataResults: MutableLiveData<List<ItemSearch>> = MutableLiveData()
    var liveDataResults : LiveData<List<ItemSearch>> = mutLiveDataResults

    val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    var statusSearchLiveData = MutableLiveData <Boolean>()

    private var mutProcessing = MutableLiveData<Boolean>()
    var processing: LiveData<Boolean> = mutProcessing

    private fun thisApiCorService(context: Context?): RestApiService {
        return RestApiService.createCorService(context)
    }

    fun getSearchResults(context: Context?, term: String):  List<ItemSearch>{
        //Context can be null, to perform UnitTest, without strategy of cache with OkHttp3

        var list = mutableListOf<ItemSearch>()
        val cache = UtilsCache.getCache(term)

        if (cache != null){

            mutLiveDataResults.value = cache
            return cache

        } else {

            mutProcessing.value = true
            coroutineScope.launch {

                val request = thisApiCorService(context).getSearchList(BuildConfig.API_KEY_URBAN_DIC, term)

                withContext(Dispatchers.Main) {
                    try {
                        val response = request.await()
                        val mListItemSearch = response
                        if (mListItemSearch != null && mListItemSearch.body() != null) {

                            //results = mListItemSearch.list as MutableList<ItemSearch>

                            list = Result.Success(response.body()!!).data.list as MutableList<ItemSearch>
                            UtilsCache.addToCache(term, Result.Success(response.body()!!).data.list)

                            mutLiveDataResults.postValue(Result.Success(response.body()!!).data.list)


                        }
                    } catch (e: HttpException) {
                        //"true" is for "API call error", can be observed outside this repository for any purpose
                        mutLiveDataResults.postValue(listOf())
                        (statusSearchLiveData as MutableLiveData).postValue(true)
                        // Or Log.e exception //

                    } catch (e: Throwable) {
                        //"true" is for "API call error", can be observed outside this repository for any purpose
                        mutLiveDataResults.postValue(listOf())
                        (statusSearchLiveData as MutableLiveData).postValue(true)

                        // Or Log.e error //)
                    } finally {
                        (statusSearchLiveData as MutableLiveData).postValue(false)
                    }


                }


            }


        }

        return list

    }


}