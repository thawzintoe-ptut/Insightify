package com.ptut.insightify

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InsightifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
