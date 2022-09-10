package com.lovepreet.gallery.ui.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.lovepreet.gallery.R
import com.lovepreet.gallery.dataSource.Error
import com.lovepreet.gallery.databinding.ActivityGalleryBinding
import com.lovepreet.gallery.extensions.showSnackbar
import com.lovepreet.gallery.models.ImageModel
import com.lovepreet.gallery.ui.gallery.adapter.GalleryImageAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {

    private val viewModel by viewModels<GalleryViewModel>()
    private lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        handleGui()
    }

    private fun handleGui(){
        viewModel.adapter.delegate = object: GalleryImageAdapter.OnImageClickListener{
            override fun onImageTapped(image: ImageModel) {
                //open image in detail with adapter
            }
        }

        viewModel.imageListObserver.observe(this){
            if(it is Error){
                showSnackbar(it.error)
            }
        }

        viewModel.loadImagesInGallery()

    }
}