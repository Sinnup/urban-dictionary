package com.sinue.streetworkout.urbandictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sinue.streetworkout.urbandictionary.model.ItemSearch

class MainViewModel: ViewModel() {

    private val itemSearchRepository = UrbanDictionaryRepository()
    var searchItemsResults: LiveData<List<ItemSearch>> = MutableLiveData<List<ItemSearch>>()

    fun searchTerm(term: String) {

        searchItemsResults = itemSearchRepository.getMutableLiveData(term)
    }

    /**
     * This methods sorts the results of the search.
     *
     *
     * @property fieldToSort the field to sort
     * @property order true if "asc", otherwise "desc"
     */
    fun sortResults(fieldToSort: String, order: Boolean) {

        if (order) {
            when (fieldToSort) {
                "thumbsUp" -> searchItemsResults.value!!.sortedWith( compareBy{ it.thumbs_up } )
                else -> {
                    searchItemsResults.value!!.sortedWith( compareBy{ it.thumbs_down } )
                }
            }

        } else {
            when (fieldToSort) {
                "thumbsUp" -> searchItemsResults.value!!.sortedWith( compareByDescending{ it.thumbs_up } )
                else -> {
                    searchItemsResults.value!!.sortedWith( compareByDescending{ it.thumbs_down } )
                }
            }
        }


    }

    override fun onCleared() {
        super.onCleared()
        return itemSearchRepository.completableJob.cancel()
    }
}