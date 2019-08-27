package com.andfactory.animation.example.details

import android.view.LayoutInflater
import android.view.ViewGroup

import com.andfactory.animation.example.R
import com.andfactory.animation.example.databinding.DetailsItemBinding

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


internal class DetailsAdapter(private val mData: List<DetailsData>) :
    RecyclerView.Adapter<DetailsItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsItem {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.details_item, parent, false)
        return DetailsItem(binding.getRoot())
    }

    override fun onBindViewHolder(holder: DetailsItem, position: Int) {
        holder.setContent(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}
