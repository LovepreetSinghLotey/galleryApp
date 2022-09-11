package com.lovepreet.gallery.ui.galleryDetail.adapter

/**
 * Created by Lovepreet Singh on 11/09/22.
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovepreet.gallery.R
import com.lovepreet.gallery.databinding.ItemGalleryBinding
import com.lovepreet.gallery.databinding.ItemImageSliderViewerBinding
import com.lovepreet.gallery.models.ImageModel
import com.lovepreet.gallery.ui.gallery.adapter.GalleryImageAdapter
import com.squareup.picasso.Picasso

class ImagesSliderViewerAdapter(
    var picasso: Picasso,
    var images: ArrayList<ImageModel>)
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
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class PagerViewHolder(val binding: ItemImageSliderViewerBinding)
        : RecyclerView.ViewHolder(binding.root)
}