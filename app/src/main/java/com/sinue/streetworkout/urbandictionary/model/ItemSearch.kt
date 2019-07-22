package com.sinue.streetworkout.urbandictionary.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItemSearch(
    @Json(name = "definition")
    var definition: String,
    @Json(name = "thumbs_up")
    var thumbs_up: Int,
    @Json(name = "thumbs_down")
    var thumbs_down: Int

)


