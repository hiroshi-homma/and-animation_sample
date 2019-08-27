package com.andfactory.animation.example.profile


import android.view.View
import android.widget.TextView

import com.andfactory.animation.example.details.DetailsData
import com.andfactory.animation.example.R

import androidx.recyclerview.widget.RecyclerView

class ProfileItem(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal fun setContent(data: DetailsData) {
        (itemView.findViewById<View>(R.id.tv_title) as TextView).text = data.title
        (itemView.findViewById<View>(R.id.tv_text) as TextView).text = data.text
    }

}
