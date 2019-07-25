package com.sinue.streetworkout.urbandictionary.utils

import com.sinue.streetworkout.urbandictionary.model.ItemSearch

class UtilsCache {

    companion object {

        private var mapCache: MutableMap<String, List<ItemSearch>> = HashMap<String, List<ItemSearch>>()

        fun getCache(key: String): List<ItemSearch>? {
            return mapCache.get(key)
        }

        fun addToCache(key: String, listItems: List<ItemSearch>) {
            //This logic warantees we're not storing empty values which are not important and generate more space
            if (listItems.isNotEmpty()) mapCache.put(key, listItems)

        }
    }
}