package com.sinue.streetworkout.urbandictionary.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListResponse (

    @Json(name="list")
    var list: List<ItemSearch>
)