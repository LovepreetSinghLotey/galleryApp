package com.lovepreet.gallery.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Lovepreet Singh on 10/09/22.
 */


data class ImageModel(
    val url: String,
    @SerializedName("hdurl") val hdUrl: String,
    val title: String,
    val explanation: String,
    val date: String,
    val copyright: String
)
