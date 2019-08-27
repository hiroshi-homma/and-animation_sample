package com.andfactory.animation.example.main.inner


import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.andfactory.animation.example.R

import org.greenrobot.eventbus.EventBus

class InnerItem(itemView: View) : com.andfactory.animation.inner.InnerItem(itemView) {

    private val mInnerLayout: View

    private val mHeader: TextView
    private val mName: TextView
    val mAddress: TextView
    private val mAvatar: ImageView
    val mAvatarBorder: View
    private val mLine: View

    var itemData: InnerData? = null
        private set

    init {
        mInnerLayout = (itemView as ViewGroup).getChildAt(0)

        mHeader = itemView.findViewById<View>(R.id.tv_header) as TextView
        mName = itemView.findViewById<View>(R.id.tv_name) as TextView
        mAddress = itemView.findViewById<View>(R.id.tv_address) as TextView
        mAvatar = itemView.findViewById<View>(R.id.avatar) as ImageView
        mAvatarBorder = itemView.findViewById(R.id.avatar_border)
        mLine = itemView.findViewById(R.id.line)

        mInnerLayout.setOnClickListener { EventBus.getDefault().post(this@InnerItem) }
    }

    override fun getInnerLayout(): View {
        return mInnerLayout
    }

    fun clearContent() {
        Glide.with(mAvatar.context).clear(mAvatar)
        itemData = null
    }

    @SuppressLint("SetTextI18n")
    internal fun setContent(data: InnerData) {
        itemData = data

        mHeader.text = data.title
        mHeader.text = data.title
        mName.text =
                data.name + "" + itemView.context.getString(R.string.answer_low)

        mAddress.text =
                data.age.toString() + " " +
                mAddress.context.getString(R.string.years) + " · " + data.address


        Glide.with(itemView.context)
            .load(data.avatarUrl)
            .placeholder(R.drawable.avatar_placeholder)
            .transform(CircleCrop())
            .into(mAvatar)
    }

}
