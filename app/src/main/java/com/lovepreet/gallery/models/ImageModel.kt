package com.lovepreet.gallery.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

@Parcelize
data class ImageModel(
    val url: String,
    @SerializedName("hdurl") val hdUrl: String,
    val title: String,
    val explanation: String,
    val date: String,
    val copyright: String?
): Parcelable
