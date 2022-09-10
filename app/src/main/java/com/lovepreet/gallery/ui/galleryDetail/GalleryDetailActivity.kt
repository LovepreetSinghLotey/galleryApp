package com.lovepreet.gallery.ui.galleryDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.lovepreet.gallery.R
import com.lovepreet.gallery.databinding.ActivityGalleryDetailBinding

class GalleryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_detail)
        binding.lifecycleOwner = this

        handleGui()
    }

    private fun handleGui(){

    }
}