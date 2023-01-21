package com.babble.babble

import android.app.Application
import com.babble.babblesdk.BabbleSDK

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        BabbleSDK.init(this, "TiHprDYCQy9UANrWWcA3")
    }
}