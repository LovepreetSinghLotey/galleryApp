package com.lovepreet.gallery.ui.galleryDetail.adapter

/**
 * Created by Lovepreet Singh on 11/09/22.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.lovepreet.gallery.R
import com.lovepreet.gallery.databinding.ItemImageSliderViewerBinding
import com.lovepreet.gallery.models.ImageModel
import com.squareup.picasso.Picasso

class ImagesSliderViewerAdapter(
    val lifecycleOwner: LifecycleOwner,
    val picasso: Picasso,
    private val images: ArrayList<ImageModel>,
    private val delegate: ImageSliderInterface)
    : RecyclerView.Adapter<ImagesSliderViewerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding: ItemImageSliderViewerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_image_slider_viewer, parent,
            false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        picasso.load(images[position].hdUrl)
            .placeholder(R.drawable.no_image_available_dark)
            .error(R.drawable.no_image_available_dark)
            .into(holder.binding.imageView)

        holder.binding.imageView.imageZoomObserver.observe(lifecycleOwner){
            if(it > 1f){
                delegate.disableSwipe()
            }
            if(it == 1f){
                delegate.enableSwipe()
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class PagerViewHolder(val binding: ItemImageSliderViewerBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface ImageSliderInterface{
        fun disableSwipe()
        fun enableSwipe()
    }
}