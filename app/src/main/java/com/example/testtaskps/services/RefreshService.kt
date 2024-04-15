package com.example.testtaskps.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.MutableLiveData
import com.example.testtaskps.modules.Qualifiers
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.services.model.RatesDataImpl
import com.example.testtaskps.services.model.RefreshApi
import com.example.testtaskps.utils.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class RefreshService : Service() {

    @Inject lateinit var ratesData: RatesData
    @Qualifiers.RefreshDataStore
    @Inject lateinit var dataStore: DataStore<Preferences>
    @Inject lateinit var refreshApi: RefreshApi
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        val FORCE_REFRESH_KEY = "force_refresh"
        val LAST_REFRESH_TIME_KEY = longPreferencesKey("lastRefreshTime")
        var isForced: Boolean = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        refreshData(intent?.extras?.containsKey(FORCE_REFRESH_KEY) == true)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun refreshData(isForced : Boolean = false) {
        refreshApi.fetchRates().enqueue(object : Callback<RatesDataImpl> {
            override fun onResponse(p0: Call<RatesDataImpl>, p1: Response<RatesDataImpl>) {
                val body = p1.body()

                if (p1.isSuccessful && body != null) {
                    val rates = (ratesData as RatesDataImpl)
                    rates.base = body.base
                    rates.date = body.date
                    rates.rates.clear()
                    rates.rates.putAll(body.rates)
                    (rates.liveData as MutableLiveData<Event<Boolean>>).value = Event(true)

                    coroutineScope.launch {
                        dataStore.edit { it[LAST_REFRESH_TIME_KEY] = System.currentTimeMillis() }
                    }

                    if (!isForced) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            refreshData()
                        }, 5000)
                    }
                } else {
                    onFailure(p0, Throwable())
                }
            }

            override fun onFailure(p0: Call<RatesDataImpl>, p1: Throwable) {
                ((ratesData as RatesDataImpl).liveData as MutableLiveData<Event<Boolean>>)
                    .value = Event(false)
            }
        })
    }
}