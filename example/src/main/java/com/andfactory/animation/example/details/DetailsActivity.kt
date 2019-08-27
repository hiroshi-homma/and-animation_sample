package com.andfactory.animation.example.details

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.andfactory.animation.example.AndFactoryApp
import com.andfactory.animation.example.main.MainActivity
import com.andfactory.animation.example.profile.ProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.andfactory.animation.example.R

import java.util.ArrayList

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.bloco.faker.Faker

class DetailsActivity : AppCompatActivity(), AndFactoryApp.FakerReadyListener {

    private val mListData = ArrayList<DetailsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        (application as AndFactoryApp).addListener(this)

        (findViewById<View>(R.id.tv_name) as TextView).text = intent.getStringExtra(BUNDLE_NAME)
        (findViewById<View>(R.id.tv_info) as TextView).text = intent.getStringExtra(BUNDLE_INFO)

        Glide.with(this)
            .load(intent.getStringExtra(BUNDLE_AVATAR_URL))
            .placeholder(R.drawable.avatar_placeholder)
            .transform(CircleCrop())
            .into(findViewById<View>(R.id.avatar) as ImageView)
    }

    override fun onFakerReady(faker: Faker?) {
        (findViewById<View>(R.id.tv_status) as TextView).text = faker!!.book.title()

        for (i in 0 until ITEM_COUNT) {
            mListData.add(DetailsData(faker.book.title(), faker.name.name()))
        }

        (findViewById<View>(R.id.recycler_view) as RecyclerView).adapter = DetailsAdapter(mListData)
    }

    fun onCloseClick(v: View) {
        super.onBackPressed()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun onDetailsClick() {
        ProfileActivity.start(
            this,
            intent.getStringExtra(BUNDLE_AVATAR_URL),
            intent.getStringExtra(BUNDLE_NAME),
            intent.getStringExtra(BUNDLE_INFO),
            (findViewById<View>(R.id.tv_status) as TextView).text.toString(),
            findViewById(R.id.avatar),
            findViewById(R.id.card),
            findViewById(R.id.iv_background),
            findViewById(R.id.recycler_view),
            mListData
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    }

    companion object {

        private val ITEM_COUNT = 4

        private val BUNDLE_NAME = "BUNDLE_NAME"
        private val BUNDLE_INFO = "BUNDLE_INFO"
        private val BUNDLE_AVATAR_URL = "BUNDLE_AVATAR_URL"

        fun start(
            activity: MainActivity,
            name: String, address: String, url: String,
            card: View, avatar: View
        ) {
            val starter = Intent(activity, DetailsActivity::class.java)

            starter.putExtra(BUNDLE_NAME, name)
            starter.putExtra(BUNDLE_INFO, address)
            starter.putExtra(BUNDLE_AVATAR_URL, url)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val p1 = Pair.create(card, activity.getString(R.string.transition_card))
                val p2 = Pair.create(avatar, activity.getString(R.string.transition_avatar_border))

                val options = ActivityOptions.makeSceneTransitionAnimation(activity, p1, p2)
                activity.startActivity(starter, options.toBundle())
            } else {
                activity.startActivity(starter)
            }
        }
    }

}
