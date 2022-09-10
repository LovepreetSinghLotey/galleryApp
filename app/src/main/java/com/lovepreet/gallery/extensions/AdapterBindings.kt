package com.lovepreet.twitterHomeUi.extensions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Lovepreet Singh on 10/09/22.
 */

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}