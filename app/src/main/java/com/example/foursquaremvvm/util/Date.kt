package com.example.foursquaremvvm.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
fun Date.now(): String {
    val sdf = SimpleDateFormat("yyyyMMdd")
    return sdf.format(this)
}
