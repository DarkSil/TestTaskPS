package com.example.testtaskps.utils

import android.content.Context
import android.content.Intent
import com.example.testtaskps.services.RefreshService

object ServiceUtil {

    fun Context.launchRefreshService(isForced: Boolean = false) {
        val intent = Intent(this, RefreshService::class.java)
        if (isForced) {
            intent.apply {
                putExtra(RefreshService.FORCE_REFRESH_KEY, true)
            }
        }
        startService(intent)
    }

}