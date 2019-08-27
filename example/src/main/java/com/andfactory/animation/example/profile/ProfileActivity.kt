package com.andfactory.animation.example.profile

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.util.Pair
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.andfactory.animation.example.details.DetailsData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.appbar.AppBarLayout
import com.andfactory.animation.example.R

import java.util.ArrayList

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {

    private val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.activity_profile)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val fullName = intent.getStringExtra(BUNDLE_NAME)
        val title =
            fullName!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] + getString(
                R.string.profile
            )

        (findViewById<View>(R.id.tv_title) as TextView).text = title
        (findViewById<View>(R.id.tv_name) as TextView).text = fullName
        (findViewById<View>(R.id.tv_info) as TextView).text = intent.getStringExtra(BUNDLE_INFO)
        (findViewById<View>(R.id.tv_status) as TextView).text = intent.getStringExtra(BUNDLE_STATUS)

        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        val listData = intent.getParcelableArrayListExtra<DetailsData>(BUNDLE_LIST_DATA)
        recyclerView.adapter = ProfileAdapter(mData = listData)

        Glide.with(this)
            .load(intent.getStringExtra(BUNDLE_AVATAR_URL))
            .placeholder(R.drawable.avatar_placeholder)
            .transform(CircleCrop())
            .into(findViewById<View>(R.id.avatar) as ImageView)

        val appBarLayout = findViewById<View>(R.id.app_bar) as AppBarLayout
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {

            val headerImage = findViewById<View>(R.id.header_image)
            val headerInfo = findViewById<View>(R.id.header_info)
            val avatar = findViewById<View>(R.id.avatar_border)
            val texts = findViewById<View>(R.id.texts) as LinearLayout

            val avatarHOffset = resources.getDimensionPixelSize(R.dimen.profile_avatar_h_offset)
            val avatarVOffset = resources.getDimensionPixelSize(R.dimen.profile_avatar_v_offset)
            val avatarSize = resources.getDimensionPixelSize(R.dimen.profile_avatar_size)
            val textHOffset = resources.getDimensionPixelSize(R.dimen.profile_texts_h_offset)
            val textVMinOffset = resources.getDimensionPixelSize(R.dimen.profile_texts_v_min_offset)
            val textVMaxOffset = resources.getDimensionPixelSize(R.dimen.profile_texts_v_max_offset)
            val textVDiff = textVMaxOffset - textVMinOffset
            val header160 = resources.getDimensionPixelSize(R.dimen.dp160)
            val toolBarHeight: Int

            val textStart = ArrayList<Float>()

            init {
                val styledAttributes = theme.obtainStyledAttributes(
                    intArrayOf(android.R.attr.actionBarSize)
                )
                toolBarHeight = styledAttributes.getDimension(0, 0f).toInt() + statusBarHeight
                styledAttributes.recycle()

                avatar.pivotX = 0f
                avatar.pivotY = 0f
                texts.pivotX = 0f
                texts.pivotY = 0f
            }

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                val diff = toolBarHeight + verticalOffset
                val y = if (diff < 0) header160 - diff else header160
                headerInfo.top = y

                val lp = headerImage.layoutParams as FrameLayout.LayoutParams
                lp.height = y
                headerImage.layoutParams = lp

                val totalScrollRange = appBarLayout.totalScrollRange
                val ratio = (totalScrollRange.toFloat() + verticalOffset) / totalScrollRange

                val avatarHalf = avatar.measuredHeight / 2
                val avatarRightest = appBarLayout.measuredWidth / 2 - avatarHalf - avatarHOffset
                val avatarTopest = avatarHalf + avatarVOffset

                avatar.x = avatarHOffset + avatarRightest * ratio
                avatar.y = avatarVOffset - avatarTopest * ratio
                avatar.scaleX = 0.5f + 0.5f * ratio
                avatar.scaleY = 0.5f + 0.5f * ratio

                if (textStart.isEmpty() && verticalOffset == 0) {
                    for (i in 0 until texts.childCount) {
                        textStart.add(texts.getChildAt(i).x)
                    }
                }

                texts.x = textHOffset + (avatarSize * 0.5f - avatarVOffset) * (1f - ratio)
                texts.y = textVMinOffset + textVDiff * ratio
                texts.scaleX = 0.8f + 0.2f * ratio
                texts.scaleY = 0.8f + 0.2f * ratio
                for (i in textStart.indices) {
                    texts.getChildAt(i).x = textStart[i] * ratio
                }
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {

                internal var isStarting = true

                override fun onTransitionStart(transition: Transition) {
                    if (isStarting) {
                        isStarting = false

                        ViewCompat.setTransitionName(findViewById(R.id.header_image), null)
                        ViewCompat.setTransitionName(findViewById(R.id.recycler_view), null)
                    }
                }

                override fun onTransitionEnd(transition: Transition) {}

                override fun onTransitionCancel(transition: Transition) {}

                override fun onTransitionPause(transition: Transition) {}

                override fun onTransitionResume(transition: Transition) {}
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    companion object {

        private val BUNDLE_NAME = "BUNDLE_NAME"
        private val BUNDLE_INFO = "BUNDLE_INFO"
        private val BUNDLE_STATUS = "BUNDLE_STATUS"
        private val BUNDLE_AVATAR_URL = "BUNDLE_AVATAR_URL"
        private val BUNDLE_LIST_DATA = "BUNDLE_LIST_DATA"

        fun start(
            activity: Activity,
            url: String, name: String, info: String, status: String,
            avatar: View, card: View, image: View, list: View,
            listData: ArrayList<DetailsData>
        ) {
            val starter = Intent(activity, ProfileActivity::class.java)
            starter.putExtra(BUNDLE_NAME, name)
            starter.putExtra(BUNDLE_INFO, info)
            starter.putExtra(BUNDLE_STATUS, status)
            starter.putExtra(BUNDLE_AVATAR_URL, url)
            starter.putParcelableArrayListExtra(BUNDLE_LIST_DATA, listData)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val p1 = Pair.create(avatar, activity.getString(R.string.transition_avatar_border))
                val p2 = Pair.create(card, activity.getString(R.string.transition_card))
                val p3 = Pair.create(image, activity.getString(R.string.transition_background))
                val p4 = Pair.create(list, activity.getString(R.string.transition_list))

                val options = ActivityOptions.makeSceneTransitionAnimation(activity, p1, p2, p3, p4)
                activity.startActivity(starter, options.toBundle())
            } else {
                activity.startActivity(starter)
            }
        }
    }

}
