package com.example.testtaskps.services.model

import retrofit2.Call
import retrofit2.http.GET

interface RefreshApi {
    @GET("tasks/api/currency-exchange-rates")
    fun fetchRates() : Call<RatesDataImpl>
}