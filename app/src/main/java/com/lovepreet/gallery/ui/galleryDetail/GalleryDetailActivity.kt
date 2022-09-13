package com.lovepreet.gallery.ui.galleryDetail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.reflect.TypeToken
import com.lovepreet.gallery.R
import com.lovepreet.gallery.databinding.ActivityGalleryDetailBinding
import com.lovepreet.gallery.extensions.fadeTransition
import com.lovepreet.gallery.models.ImageModel
import com.lovepreet.gallery.ui.galleryDetail.adapter.ImagesSliderViewerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryDetailActivity : AppCompatActivity() {

    companion object{
        const val IMAGE_LIST: String = "imageList"
        const val INDEX: String = "index"
    }

    private lateinit var binding: ActivityGalleryDetailBinding
    private val viewModel by viewModels<GalleryDetailViewModel>()

    private var currentItemPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_detail)
        binding.lifecycleOwner = this

        handleGui()
    }

    private fun handleGui(){
        binding.back.setOnClickListener {
            onBackPressed()
        }
        handleViewPager()
    }

    private fun handleViewPager(){
        currentItemPosition = intent.getIntExtra(INDEX, 0)
        val imageList: ArrayList<ImageModel> = intent.getParcelableArrayListExtra(IMAGE_LIST) ?: ArrayList()
        val imagesAdapter = ImagesSliderViewerAdapter(
            this, viewModel.picasso, imageList,
            object: ImagesSliderViewerAdapter.ImageSliderInterface{
                override fun disableSwipe() {
                    binding.toolbar.visibility = View.GONE
                    binding.desc.visibility = View.GONE
                    binding.imageViewPager.isUserInputEnabled = false
                }
                override fun enableSwipe() {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.desc.visibility = View.VISIBLE
                    binding.imageViewPager.isUserInputEnabled = true
                }
            })
        binding.imageViewPager.offscreenPageLimit = 6
        binding.imageViewPager.adapter = imagesAdapter

        binding.imageViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentItemPosition = position
                setImageDetail(imageList[currentItemPosition])
            }
        })

        binding.imageViewPager.setCurrentItem(currentItemPosition, false)
        setImageDetail(imageList[currentItemPosition])
    }

    private fun setImageDetail(image: ImageModel){
        binding.title.text = image.title
        binding.date.text = image.date
        binding.desc.text = "${image.explanation}\n\n© ${image.copyright}"
    }

    override fun onBackPressed() {
        finish()
        fadeTransition()
    }
}