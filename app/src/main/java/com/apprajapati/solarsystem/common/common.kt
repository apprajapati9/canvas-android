package com.apprajapati.solarsystem.common

import android.graphics.Color
import kotlin.random.Random

fun getRandomColor(): Int{
    return Color.argb(255,
        Random.nextInt(256),
        Random.nextInt(256),
        Random.nextInt(256))
}
