package com.sinue.streetworkout.urbandictionary.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.utils.UtilsCache

class MainViewModelImpl: MainViewModel, ViewModel() {

    val itemSearchRepository = UrbanDictionaryRepository()
    var searchItemsResults: MutableLiveData<List<ItemSearch>> = MutableLiveData<List<ItemSearch>>()
    var waitingForResponse: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    override fun searchTerm(context: Context, term: String) {

        waitingForResponse.value = true
        val cache = UtilsCache.getCache(term)
        if (cache != null) {
            searchItemsResults.value = cache
            waitingForResponse.value = false
        } else {
            searchItemsResults = itemSearchRepository.getMutableLiveData(context, term)
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
                    searchItemsResults.value = auxList.sortedWith( compareBy{ it.thumbs_up } )
                }
                else -> {
                    searchItemsResults.value = auxList.sortedWith( compareBy{ it.thumbs_down } )
                }
            }

        } else {
            when (fieldToSort) {
                "thumbsUp" -> {
                    searchItemsResults.value = auxList.sortedWith( compareByDescending{ it.thumbs_up } )
                }
                else -> {
                    searchItemsResults.value = auxList.sortedWith( compareByDescending{ it.thumbs_down } )
                }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        return itemSearchRepository.completableJob.cancel()
    }
}