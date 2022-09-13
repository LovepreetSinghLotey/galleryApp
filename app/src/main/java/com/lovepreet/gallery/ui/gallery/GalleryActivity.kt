package com.lovepreet.gallery.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lovepreet.gallery.R
import com.lovepreet.gallery.dataSource.Error
import com.lovepreet.gallery.databinding.ActivityGalleryBinding
import com.lovepreet.gallery.extensions.fadeTransition
import com.lovepreet.gallery.extensions.showSnackbar
import com.lovepreet.gallery.ui.gallery.adapter.GalleryImageAdapter
import com.lovepreet.gallery.ui.galleryDetail.GalleryDetailActivity
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
            override fun onImageTapped(index: Int, view: View) {
                startActivity(Intent(this@GalleryActivity, GalleryDetailActivity::class.java)
                    .putExtra(GalleryDetailActivity.INDEX, index)
                    .putParcelableArrayListExtra(GalleryDetailActivity.IMAGE_LIST, viewModel.adapter.images))
                fadeTransition()
            }
        }

        viewModel.imageListObserver.observe(this){
            if(it is Error){
                showSnackbar(it.error)
            }
        }
    }
}