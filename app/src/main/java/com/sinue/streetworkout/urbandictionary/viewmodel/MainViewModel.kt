package com.sinue.streetworkout.urbandictionary.viewmodel

import android.content.Context

interface MainViewModel {

    fun searchTerm(context: Context, term: String)

    fun sortResults(fieldToSort: String, order: Boolean)

}