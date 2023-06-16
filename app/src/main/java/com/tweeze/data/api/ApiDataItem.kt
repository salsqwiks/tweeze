package com.tweeze.data.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApiDataItem(
    val Place_Name: String,
    val Category: String,
    val Description: String,
    val City: String,
    val Dos_1: String,
    val Dos_2: String,
    val Donts_1: String,
    val Donts_2: String,
    val photo_URL: String,
) : Parcelable