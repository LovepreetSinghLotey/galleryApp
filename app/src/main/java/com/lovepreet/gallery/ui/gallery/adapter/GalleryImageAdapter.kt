package com.lovepreet.gallery.ui.gallery.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lovepreet.gallery.R
import com.lovepreet.gallery.databinding.ItemGalleryBinding
import com.lovepreet.gallery.models.ImageModel
import com.lovepreet.gallery.ui.customUi.bounceViewAnimation.BounceView
import com.squareup.picasso.Picasso

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

class GalleryImageAdapter(private var picasso: Picasso)
    : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {

    val images: ArrayList<ImageModel> = ArrayList()
    var delegate: OnImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemGalleryBinding = DataBindingUtil.inflate(
                                                LayoutInflater.from(parent.context),
                                                R.layout.item_gallery, parent,
                                                false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        picasso.load(images[position].url)
            .placeholder(R.drawable.no_image_available_dark)
            .error(R.drawable.no_image_available_dark)
            .into(holder.binding.imageView)

        holder.binding.title.text = images[position].title

        BounceView.addAnimTo(holder.binding.root)
        holder.binding.root.setOnClickListener{
            delegate?.onImageTapped(position, holder.binding.root)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(feeds: ArrayList<ImageModel>){
        this.images.clear()
        this.images.addAll(feeds)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        images.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemGalleryBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface OnImageClickListener{
        fun onImageTapped(index: Int, view: View)
    }
}