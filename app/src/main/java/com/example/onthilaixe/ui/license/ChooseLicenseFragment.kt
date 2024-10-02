package com.example.onthilaixe.ui.license

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.onthilaixe.base.BaseFragmentWithBinding
import com.example.onthilaixe.databinding.FragmentChooseLicenseBinding
import com.example.onthilaixe.models.local.sharepreference.Preferences
import com.example.onthilaixe.ui.home.HomeFragment
import com.example.onthilaixe.utils.Constants
import com.example.onthilaixe.viewmodel.LicenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChooseLicenseFragment : BaseFragmentWithBinding<FragmentChooseLicenseBinding>() {

    private val licenseViewModel: LicenseViewModel by viewModels()
    private lateinit var licenseAdapter: ChooseLicenseAdapter

    @Inject
    lateinit var preferences: Preferences
    override fun getViewBinding(inflater: LayoutInflater): FragmentChooseLicenseBinding {
        return FragmentChooseLicenseBinding.inflate(layoutInflater)
    }

    override fun init() {
        setupAdapter()
    }

    override fun initData() {
        licenseViewModel.getALlLicense().observe(viewLifecycleOwner) { listLicense ->
            licenseAdapter.submitList(listLicense)
        }
    }

    override fun initAction() {
        //do nothing
    }

    private fun setupAdapter() {
        val nameLicense = preferences.getString(Constants.KEY_LICENSE_TYPE)
        licenseAdapter = ChooseLicenseAdapter(nameLicense) { license ->
            license.name?.let { name ->
                preferences.setString(Constants.KEY_LICENSE_TYPE, name)
            }
            license.numberOfCorrectQuestion?.let { numberOfCorrectQuestion ->
                preferences.setInt(Constants.KEY_NUMBER_OF_CORRECT_QUEST, numberOfCorrectQuestion)
            }
            license.duration?.let { duration ->
                preferences.setFloat(Constants.KEY_DURATION, duration)
            }
            license.numberOfQuestion?.let { numberOfQuestion ->
                preferences.setInt(Constants.KEY_NUMBER_OF_QUEST, numberOfQuestion)
            }
            openFragment(HomeFragment::class.java, null, false)
        }
        binding.rcLicense.adapter = licenseAdapter
    }
}