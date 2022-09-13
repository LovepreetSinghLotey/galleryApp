package com.lovepreet.gallery.ui.galleryDetail

import android.view.View
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Lovepreet Singh on 11/09/22.
 */

@HiltViewModel
class GalleryDetailViewModel @Inject constructor(
    var picasso: Picasso
): ViewModel()