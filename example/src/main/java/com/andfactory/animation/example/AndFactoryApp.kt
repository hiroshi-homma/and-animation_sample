package com.andfactory.animation.example

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication

import java.lang.ref.WeakReference
import java.util.ArrayList
import io.bloco.faker.Faker
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import androidx.multidex.MultiDex

class AndFactoryApp : MultiDexApplication() {

    private var mFaker: Faker? = null

    private val mListeners = ArrayList<WeakReference<FakerReadyListener>>()

    interface FakerReadyListener {
        fun onFakerReady(faker: Faker?)
    }

    override fun onCreate() {
        super.onCreate()
        initFaker()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun addListener(listener: FakerReadyListener) {
        mListeners.add(WeakReference(listener))
        if (mFaker != null) {
            listener.onFakerReady(mFaker)
        }
    }

    fun removeListener(listener: FakerReadyListener) {
        for (wRef in mListeners) {
            if (wRef.get() === listener) {
                mListeners.remove(wRef)
                break
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initFaker() {
        Single.create(SingleOnSubscribe<Faker> { e ->
            val faker = Faker()

            if (!e.isDisposed) {
                e.onSuccess(faker)
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { faker ->
                mFaker = faker

                for (wRef in mListeners) {
                    val listener = wRef.get()
                    listener?.onFakerReady(mFaker)
                }
            }
    }

}
