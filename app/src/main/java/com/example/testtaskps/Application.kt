package com.example.testtaskps

import android.app.Application
import android.content.Intent
import com.example.testtaskps.services.RefreshService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startService(Intent(this, RefreshService::class.java))
    }

}