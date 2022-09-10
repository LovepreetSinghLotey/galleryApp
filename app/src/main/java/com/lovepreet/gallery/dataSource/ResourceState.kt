package com.lovepreet.gallery.dataSource

import com.lovepreet.gallery.models.ImageModel

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

sealed class ResourceState
data class Success(var data: ArrayList<ImageModel>): ResourceState()
data class Error(var error: String): ResourceState()