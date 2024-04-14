package com.example.testtaskps.services.model

import androidx.lifecycle.LiveData
import com.example.testtaskps.utils.Event

interface RatesData {
    val liveData : LiveData<Event<Boolean>>
    fun getBaseCurrency() : String
    fun getMapOfRates() : Map<String, Float>
}