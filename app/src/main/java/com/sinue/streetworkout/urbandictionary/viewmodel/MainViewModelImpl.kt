package com.sinue.streetworkout.urbandictionary.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.utils.UtilsCache

class MainViewModelImpl : MainViewModel, ViewModel() {

    private val itemSearchRepository = UrbanDictionaryRepository()

    private var mutableSearchItemsResults: MutableLiveData<List<ItemSearch>> = MutableLiveData()
    var searchItemsResults: LiveData<List<ItemSearch>> = MutableLiveData()
    var requesting: LiveData<Boolean> = MutableLiveData()
    var waitingForResponse: MutableLiveData<Boolean> = MutableLiveData()

    init {
        waitingForResponse.postValue(false)

    }

    override fun searchTerm(context: Context, term: String) {

        itemSearchRepository.requesting.observeForever {
            requesting = itemSearchRepository.requesting
        }

        val cache = UtilsCache.getCache(term)
        waitingForResponse.value = false
        if (cache != null) {
            mutableSearchItemsResults.value = cache
            searchItemsResults = mutableSearchItemsResults

        } else {
            searchItemsResults = itemSearchRepository.getSearchResults(context, term)
            waitingForResponse.value = false
        }


    }

    /**
     * This method sorts the results of the search.
     * @property fieldToSort the field to sort
     * @property order true if "asc", otherwise "desc"
     */
    override fun sortResults(fieldToSort: String, order: Boolean) {

        val auxList: MutableList<ItemSearch> = ArrayList(searchItemsResults.value!!)

        if (order) {
            when (fieldToSort) {
                "thumbsUp" -> {
                    mutableSearchItemsResults.value = auxList.sortedWith(compareBy { it.thumbs_up })
                }
                else -> {
                    mutableSearchItemsResults.value = auxList.sortedWith(compareBy { it.thumbs_down })
                }
            }

        } else {
            when (fieldToSort) {
                "thumbsUp" -> {
                    mutableSearchItemsResults.value = auxList.sortedWith(compareByDescending { it.thumbs_up })
                }
                else -> {
                    mutableSearchItemsResults.value = auxList.sortedWith(compareByDescending { it.thumbs_down })
                }
            }
        }

        searchItemsResults = mutableSearchItemsResults

    }

    override fun onCleared() {
        super.onCleared()
        return itemSearchRepository.completableJob.cancel()
    }
}