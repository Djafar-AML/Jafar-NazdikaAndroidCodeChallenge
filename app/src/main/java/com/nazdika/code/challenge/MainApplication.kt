package com.nazdika.code.challenge

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp
import android.provider.Settings


lateinit var androidId: String

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        initAndroidId()
    }

    private fun initAndroidId() {
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

}