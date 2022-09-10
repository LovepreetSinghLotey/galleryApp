package com.lovepreet.gallery.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovepreet.gallery.dataSource.ImageDataSource
import com.lovepreet.gallery.dataSource.ResourceState
import com.lovepreet.gallery.dataSource.Success
import com.lovepreet.gallery.ui.gallery.adapter.GalleryImageAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private var picasso: Picasso,
    private var imageDataSource: ImageDataSource
): ViewModel() {

    val imageListObserver: MutableLiveData<ResourceState> = MutableLiveData()

    val adapter = GalleryImageAdapter(picasso)

    fun loadImagesInGallery(){
        viewModelScope.launch(Dispatchers.Main) {
            val response: ResourceState = withContext(Dispatchers.IO){
                imageDataSource.getImages()
            }
            if(response is Success){
                adapter.addAll(response.data)
            } else{
                imageListObserver.value = response
            }
        }
    }

}