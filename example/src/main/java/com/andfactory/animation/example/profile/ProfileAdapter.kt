package com.andfactory.animation.example.profile

import android.view.LayoutInflater
import android.view.ViewGroup

import com.andfactory.animation.example.details.DetailsData
import com.andfactory.animation.example.R
import com.andfactory.animation.example.databinding.ProfileItemBinding

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


internal class ProfileAdapter(private val mData: List<DetailsData>) :
    RecyclerView.Adapter<ProfileItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileItem {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.profile_item, parent, false)
        return ProfileItem(binding.getRoot())
    }

    override fun onBindViewHolder(holder: ProfileItem, position: Int) {
        holder.setContent(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}
