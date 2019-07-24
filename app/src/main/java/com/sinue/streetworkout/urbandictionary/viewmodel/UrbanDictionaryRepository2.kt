package com.sinue.streetworkout.urbandictionary.viewmodel

import com.sinue.streetworkout.urbandictionary.BuildConfig
import com.sinue.streetworkout.urbandictionary.model.ItemSearch
import com.sinue.streetworkout.urbandictionary.networking.RestApiService

class UrbanDictionaryRepository2(private val api: RestApiService): BaseRepository() {

    suspend fun getResults(term: String) : List<ItemSearch> {

        val searchResponse = safeApiCall(
            call = {api.getSearchList(BuildConfig.API_KEY_URBAN_DIC, term).await()},
            errorMessage = "Error fetching data"
        )

        return if (searchResponse != null) searchResponse.list else listOf<ItemSearch>()
    }

}