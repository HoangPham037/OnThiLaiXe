package com.example.onthilaixe.ui.splash

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.databinding.FragmentSplashBinding
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.home.HomeFragment
import com.example.onthilaixe.ui.license.ChooseLicenseFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragmentWithBinding<FragmentSplashBinding>() {
    private val splashViewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var preferences: Preferences
    override fun getViewBinding(inflater: LayoutInflater): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(layoutInflater)
    }

    override fun init() {
    }

    override fun initData() {
        //do nothing
    }

    override fun initAction() {
        splashViewModel.progress.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it
        }
        splashViewModel.max.observe(viewLifecycleOwner) {
            binding.progressBar.max = it
        }

        splashViewModel.navigateToNextScreen.observe(viewLifecycleOwner) {
            val getLicense = preferences.getString(Constants.KEY_LICENSE_TYPE)
            if (getLicense != null) {
                openFragment(HomeFragment::class.java, null, false)
            } else {
                openFragment(ChooseLicenseFragment::class.java, null, false)
            }
        }
        splashViewModel.startDelay()
    }

}