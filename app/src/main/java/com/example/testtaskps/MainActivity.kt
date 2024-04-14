package com.example.testtaskps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.testtaskps.databinding.ActivityMainBinding
import com.example.testtaskps.services.RefreshService
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.utils.Event
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    lateinit var ratesData: RatesData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            ratesData.liveData.value?.value
            startService(Intent(this, RefreshService::class.java).apply {
                putExtra(RefreshService.FORCE_REFRESH_KEY, true)
            })

            ratesData.liveData.observe(this, object: Observer<Event<Boolean>> {
                override fun onChanged(value: Event<Boolean>) {
                    value.getValueIfNotHandled()?.let {
                        println("CHANGED : $it : ${ratesData.getMapOfRates()["USD"]}")
                        ratesData.liveData.removeObserver(this)
                    }
                }
            })
        }
    }
}