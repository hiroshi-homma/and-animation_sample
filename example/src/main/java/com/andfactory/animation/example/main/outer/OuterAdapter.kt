package com.andfactory.animation.example.main.outer


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.andfactory.animation.TailAdapter
import com.andfactory.animation.example.main.inner.InnerData
import com.andfactory.animation.example.R

import androidx.recyclerview.widget.RecyclerView

class OuterAdapter(private val mData: List<List<InnerData>>) : TailAdapter<OuterItem>() {

    private val POOL_SIZE = 16
    private val mPool: RecyclerView.RecycledViewPool

    init {

        mPool = RecyclerView.RecycledViewPool()
        mPool.setMaxRecycledViews(0, POOL_SIZE)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterItem {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return OuterItem(view, mPool)
    }

    override fun onBindViewHolder(holder: OuterItem, position: Int) {
        holder.setContent(mData[position])
    }

    override fun onViewRecycled(holder: OuterItem) {
        holder.clearContent()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.outer_item
    }

}
