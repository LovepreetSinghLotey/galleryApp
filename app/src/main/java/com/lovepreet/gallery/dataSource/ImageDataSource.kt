package com.lovepreet.gallery.dataSource

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lovepreet.gallery.models.ImageModel

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

class ImageDataSource(var context: Context, var gson: Gson) {

    fun getImages(): ResourceState{
        return try {
            val jsonString = context.assets.open("data.json", AssetManager.ACCESS_STREAMING)
                .bufferedReader()
                .use { it.readText() }
            val listImageType = object : TypeToken<ArrayList<ImageModel>>(){}.type
            Success(gson.fromJson(jsonString, listImageType))
        } catch (ignored: Exception){
            Error("Some error occurred while parsing !")
        }
    }
}