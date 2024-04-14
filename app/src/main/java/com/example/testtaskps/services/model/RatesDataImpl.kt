package com.example.testtaskps.services.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testtaskps.utils.Event

data class RatesDataImpl(
    var base: String = "",
    var date: String = "",
    val rates: HashMap<String, Float> = hashMapOf(),
    override val liveData: LiveData<Event<Boolean>> = MutableLiveData()
) : RatesData {
    override fun getBaseCurrency(): String {
        return base
    }

    override fun getMapOfRates(): Map<String, Float> {
        return rates.toMap()
    }

}
