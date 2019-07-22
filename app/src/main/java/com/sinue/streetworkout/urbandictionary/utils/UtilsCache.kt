package com.sinue.streetworkout.urbandictionary.utils

import com.sinue.streetworkout.urbandictionary.model.ItemSearch

class UtilsCache {

    companion object {

        private var mapCache: MutableMap<String, List<ItemSearch>> = HashMap<String, List<ItemSearch>>()

        fun getCache(key: String): List<ItemSearch>? {
            return mapCache.get(key)
        }

        fun addToCache(key: String, listItems: List<ItemSearch>) {
            mapCache.put(key, listItems)

        }
    }
}