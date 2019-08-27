package com.andfactory.animation.example.details


import android.view.View
import android.widget.TextView

import com.andfactory.animation.example.R

import androidx.recyclerview.widget.RecyclerView

internal class DetailsItem(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setContent(data: DetailsData) {
        (itemView.findViewById<View>(R.id.tv_title) as TextView).text = data.title
        (itemView.findViewById<View>(R.id.tv_text) as TextView).text = data.text
    }

}
