package com.sinue.streetworkout.urbandictionary.viewmodel

import androidx.lifecycle.LiveData
import com.sinue.streetworkout.urbandictionary.model.ItemSearch

interface MainViewModel {

    fun searchTerm(term: String)

    fun sortResults(fieldToSort: String, order: Boolean): LiveData<List<ItemSearch>>

}