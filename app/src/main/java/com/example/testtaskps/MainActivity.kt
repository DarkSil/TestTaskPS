package com.example.testtaskps

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.testtaskps.databinding.ActivityMainBinding
import com.example.testtaskps.services.RefreshService
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.utils.DefaultErrorDialog
import com.example.testtaskps.utils.DialogUtil
import com.example.testtaskps.utils.ServiceUtil.launchRefreshService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var dialog = DialogUtil.prepareDialog(DefaultErrorDialog.DialogType.REFRESH, {
        launchRefreshService()
    })

    @Inject
    lateinit var ratesData: RatesData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTransparentUI()

        setContentView(binding.root)

        launchRefreshService()

        ratesData.liveData.observe(this) { value ->
            if (!RefreshService.isForced) {
                if (!value.value) {
                    dialog.show(supportFragmentManager, null)
                    dialog = dialog.recreate()
                }
            } else {
                RefreshService.isForced = false
            }
        }
    }

    override fun onDestroy() {
        stopService(Intent(this, RefreshService::class.java))
        super.onDestroy()
    }

    private fun applyTransparentUI() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}