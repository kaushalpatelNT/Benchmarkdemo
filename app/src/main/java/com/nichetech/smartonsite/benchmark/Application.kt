package com.nichetech.smartonsite.benchmark

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication


class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }

}