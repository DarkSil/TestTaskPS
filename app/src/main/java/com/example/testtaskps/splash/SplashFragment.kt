package com.example.testtaskps.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.testtaskps.R
import com.example.testtaskps.databinding.FragmentSplashBinding
import com.example.testtaskps.main.MainFragment
import com.example.testtaskps.services.model.RatesData
import com.example.testtaskps.utils.Event
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }
    @Inject lateinit var ratesData: RatesData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratesData.liveData.observe(viewLifecycleOwner, object : Observer<Event<Boolean>> {
            override fun onChanged(value: Event<Boolean>) {
                if (value.value) {
                    parentFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragmentContainerView, MainFragment())
                        .commit()
                    ratesData.liveData.removeObserver(this)
                }
            }
        })
    }

}