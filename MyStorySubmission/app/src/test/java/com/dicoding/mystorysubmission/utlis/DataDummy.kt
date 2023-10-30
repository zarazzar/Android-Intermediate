package com.dicoding.mystorysubmission.utlis

import com.dicoding.mystorysubmission.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                "id + $i",
                "name + $i",
                "desc $i",
                "photo $i",
                "createdAt $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(stories)
        }
        return items
    }
}