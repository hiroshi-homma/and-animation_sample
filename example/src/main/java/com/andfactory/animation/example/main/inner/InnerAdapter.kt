package com.andfactory.animation.example.main.inner

import android.view.LayoutInflater
import android.view.ViewGroup

import com.andfactory.animation.example.R
import com.andfactory.animation.example.databinding.InnerItemBinding

import java.util.ArrayList
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


class InnerAdapter : com.andfactory.animation.inner.InnerAdapter<InnerItem>() {

    private val mData = ArrayList<InnerData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerItem {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return InnerItem(binding.root)
    }

    override fun onBindViewHolder(holder: InnerItem, position: Int) {
        holder.setContent(mData[position])
    }

    override fun onViewRecycled(holder: InnerItem) {
        holder.clearContent()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.inner_item
    }

    fun addData(innerDataList: List<InnerData>) {
        val size = mData.size
        mData.addAll(innerDataList)
        notifyItemRangeInserted(size, innerDataList.size)
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

}