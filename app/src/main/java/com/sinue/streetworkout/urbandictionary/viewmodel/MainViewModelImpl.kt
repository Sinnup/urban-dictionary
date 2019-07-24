package com.sinue.streetworkout.urbandictionary.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService
import com.sinue.streetworkout.urbandictionary.utils.UtilsCache
import kotlinx.coroutines.*

class MainViewModelImpl : MainViewModel, ViewModel() {

    private val itemSearchRepository = UrbanDictionaryRepository()
    private val repository: UrbanDictionaryRepository2 = UrbanDictionaryRepository2(RestApiService.createCorService())
    var searchItemsResults: LiveData<List<ItemSearch>> = itemSearchRepository.liveDataResults
    var processing: LiveData<Boolean> = MutableLiveData()
    private val viewModelJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun searchTerm(term: String) {
        //Make sure you always update the value but not the observer object itself by assigning to another object,
        //if you are observing that object, then by assigning to another object, but the value, the observable will loss
        //track of the initial object to observe, therefore the observe will nbever work again
        //
        val cache = UtilsCache.getCache(term)
        if (cache != null){
            (searchItemsResults as MutableLiveData).postValue(cache)

        } else {

            coroutineScope.launch {
                (processing as MutableLiveData).postValue(true)
                withContext(Dispatchers.IO){
                    val vl = repository.getResults(term)
                    (searchItemsResults as MutableLiveData).postValue(vl)
                }
                (processing as MutableLiveData).postValue(false)
            }

        }
    }

    /**
     * This method sorts the results of the search.
     * @property fieldToSort the field to sort
     * @property order true if "asc", otherwise "desc"
     */
    override fun sortResults(fieldToSort: String, order: Boolean): LiveData<List<ItemSearch>> {

        coroutineScope.launch {
            withContext(Dispatchers.IO){
                val auxList: MutableList<ItemSearch> = ArrayList(searchItemsResults.value!!)

                if (order) {
                    when (fieldToSort) {
                        "thumbsUp" -> {
                            (searchItemsResults as MutableLiveData)
                                .postValue(auxList.sortedWith(compareBy { it.thumbs_up }))
                        }
                        else -> {
                            (searchItemsResults as MutableLiveData)
                                .postValue(auxList.sortedWith(compareBy { it.thumbs_down }))
                        }
                    }

                } else {
                    when (fieldToSort) {
                        "thumbsUp" -> {
                            (searchItemsResults as MutableLiveData)
                                .postValue(auxList.sortedWith(compareByDescending { it.thumbs_up }))
                        }
                        else -> {
                            (searchItemsResults as MutableLiveData)
                                .postValue(auxList.sortedWith(compareByDescending { it.thumbs_down }))
                        }
                    }
                }
            }
        }

        return searchItemsResults
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        itemSearchRepository.completableJob.cancel()
    }
}