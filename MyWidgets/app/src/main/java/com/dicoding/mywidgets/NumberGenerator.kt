package com.dicoding.mywidgets

import java.util.*

internal object NumberGenerator {
    //helper class
    fun generate(max: Int): Int {
        val random = Random()
        return random.nextInt(max)
    }
}