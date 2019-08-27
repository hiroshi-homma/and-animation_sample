package com.andfactory.animation.example.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View

import com.andfactory.animation.TailLayoutManager
import com.andfactory.animation.TailRecyclerView
import com.andfactory.animation.TailSnapHelper
import com.andfactory.animation.example.AndFactoryApp
import com.andfactory.animation.example.details.DetailsActivity
import com.andfactory.animation.example.main.inner.InnerData
import com.andfactory.animation.example.main.inner.InnerItem
import com.andfactory.animation.example.main.outer.OuterAdapter
import com.andfactory.animation.header.HeaderTransformer
import com.andfactory.animation.example.R

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import java.util.ArrayList

import androidx.appcompat.app.AppCompatActivity
import io.bloco.faker.Faker
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity(), AndFactoryApp.FakerReadyListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as AndFactoryApp).addListener(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("CheckResult")
    override fun onFakerReady(faker: Faker?) {
        Single.create(SingleOnSubscribe<List<List<InnerData>>> { e ->
            val outerData = ArrayList<List<InnerData>>()
            var i = 0
            while (i < OUTER_COUNT && !e.isDisposed) {
                val innerData = ArrayList<InnerData>()
                var j = 0
                while (j < INNER_COUNT && !e.isDisposed) {
                    innerData.add(createInnerData(faker!!))
                    j++
                }
                outerData.add(innerData)
                i++
            }

            if (!e.isDisposed) {
                e.onSuccess(outerData)
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { outerData -> initRecyclerView(outerData) }
    }

    private fun initRecyclerView(data: List<List<InnerData>>) {
        findViewById<View>(R.id.progressBar).visibility = View.GONE

        val rv = findViewById<View>(R.id.recycler_view) as TailRecyclerView
        (rv.layoutManager as TailLayoutManager).setPageTransformer(HeaderTransformer())
        rv.adapter = OuterAdapter(data)

        TailSnapHelper().attachToRecyclerView(rv)
    }

    private fun createInnerData(faker: Faker): InnerData {
        return InnerData(
            faker.book.title(),
            faker.name.name(),
            faker.address.city() + ", " + faker.address.stateAbbr(),
            faker.avatar.image(faker.internet.email(), "150x150", "jpg", "set1", "bg1"),
            faker.number.between(20, 50)
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun OnInnerItemClick(item: InnerItem) {
        val itemData = item.itemData ?: return

        DetailsActivity.start(
            this,
            item.itemData!!.name, item.mAddress.text.toString(),
            item.itemData!!.avatarUrl, item.itemView, item.mAvatarBorder
        )
    }

    companion object {

        private val OUTER_COUNT = 10
        private val INNER_COUNT = 20
    }

}
