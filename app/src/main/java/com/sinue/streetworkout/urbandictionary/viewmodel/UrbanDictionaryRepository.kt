package com.sinue.streetworkout.urbandictionary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import kotlinx.coroutines.*
import retrofit2.HttpException

class UrbanDictionaryRepository {

    private var results = mutableListOf<ItemSearch>()
    private var mutableLiveData = MutableLiveData<List<ItemSearch>>()
    val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
    var statusSearchLiveData = MutableLiveData <Boolean>()

    private val thisApiCorService by lazy {
        RestApiService.createCorService()
    }

    fun getMutableLiveData(term: String): MutableLiveData<List<ItemSearch>> {

        coroutineScope.launch {
            val request = thisApiCorService.getSearchList(BuildConfig.API_KEY_URBAN_DIC, term)
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    val mListItemSearch = response
                    if (mListItemSearch != null && mListItemSearch.list != null) {
                        results = mListItemSearch.list as MutableList<ItemSearch>
                        mutableLiveData.value = results
                    }
                } catch (e: HttpException) {
                    statusSearchLiveData.value = true
                    // Or Log.e exception //

                } catch (e: Throwable) {
                    statusSearchLiveData.value = true
                    // Or Log.e error //)
                }

            }
        }

        return mutableLiveData

    }
}