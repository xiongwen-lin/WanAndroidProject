package com.apemans.tuyahome.component.ui.mixhome

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.apemans.quickui.multitype.observeItemsChanged
import com.apemans.quickui.multitype.submitItems
import com.apemans.quickui.superdialog.SmartDialog
import com.apemans.router.routerServices
import com.apemans.router.startRouterActivity
import com.apemans.tuya.module.api.ACTIVITY_PATH_ADD_TUYA_DEVICE
import com.apemans.tuya.module.api.TuyaService
import com.apemans.tuyahome.api.FRAGMENT_PATH_MIX_HOME
import com.apemans.tuyahome.component.R
import com.apemans.tuyahome.component.databinding.TuyahomeFragmentMixHomeBinding
import com.apemans.tuyahome.component.repository.DataRepository
import com.apemans.tuyahome.component.ui.mixhome.items.DeviceViewDelegate
import com.dylanc.longan.*
import com.dylanc.longan.design.addOnTabSelectedListener
import com.dylanc.longan.design.addTab

@Route(path = FRAGMENT_PATH_MIX_HOME)
class MixHomeFragment : BaseComponentFragment<TuyahomeFragmentMixHomeBinding>() {
    private val adapter = MultiTypeAdapter(DeviceViewDelegate())
    private val viewModel: MixHomeViewModel by viewModels()
    private val networkAvailableLiveData = NetworkAvailableLiveData()
    private val tuyaService: TuyaService by routerServices()

    override fun onViewCreated(root: View) {
        binding.header.addStatusBarHeightToMarginTop()
        binding.recyclerView.adapter = adapter
        viewModel.categoryList.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty() || list.size == 1) {
                binding.tabLayout.isVisible = false
            } else {
                binding.tabLayout.isVisible = true
                binding.tabLayout.removeAllTabs()
                binding.tabLayout.addTab { text = "ALL" }
                list.forEach {
                    binding.tabLayout.addTab { text = it }
                }
            }
        }
        viewModel.initDevices(viewLifecycleOwner)
        viewModel.deviceList.observe(viewLifecycleOwner) {
            adapter.items = it
            adapter.notifyDataSetChanged()
        }
//        adapter.observeItemsChanged(viewLifecycleOwner, viewModel.deviceList) { old, new ->
//            old.uuid == new.uuid
//        }
        binding.tabLayout.addOnTabSelectedListener(
            onTabSelected = { tab ->
                viewModel.updateCategory(tab.text!!)
            },
            onTabReselected = {

            })
        binding.layoutWeakTip.root.isVisible = !isNetworkAvailable
        networkAvailableLiveData.observe(viewLifecycleOwner) {
            binding.layoutWeakTip.root.isVisible = !it
        }

        if (DataRepository.isFirstLaunch) {
            binding.tvTitle.setText(R.string.nice_to_meet_you)
            DataRepository.isFirstLaunch = false
        } else {
            binding.tvTitle.setText(R.string.welcome_back)
        }

        binding.layoutWeather.root.doOnClick {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        binding.btnAdd.doOnClick {
            startRouterActivity(ACTIVITY_PATH_ADD_TUYA_DEVICE)
        }
    }

    override fun onResume() {
        super.onResume()
        getWeather()
    }

    private val requestPermissionLauncher = requestPermissionLauncher(
        onGranted = {
            getWeather()
        },
        onDenied = {
            SmartDialog.build(parentFragmentManager, viewLifecycleOwner)
                .setTitle(getString(R.string.dialog_tip_title))
                .setContentText(getString(R.string.enable_location_tips))
                .setPositiveTextName(getString(R.string.settings))
                .setOnPositive {
                    launchAppSettings()
                }
                .setNegativeTextName(getString(R.string.cancel))
                .setOnNegative { it.dismiss() }
                .show()
        },
        onShowRequestRationale = {
            SmartDialog.build(parentFragmentManager, viewLifecycleOwner)
                .setTitle(getString(R.string.dialog_tip_title))
                .setContentText(getString(R.string.enable_location_tips))
                .setPositiveTextName(getString(R.string.settings))
                .setOnPositive {
                    requestPermissionAgain()
                }
                .setNegativeTextName(getString(R.string.cancel))
                .setOnNegative { it.dismiss() }
                .show()
        }
    )

    private fun getWeather() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        val locationProvider = when {
            providers.contains(LocationManager.NETWORK_PROVIDER) -> {
                LocationManager.NETWORK_PROVIDER
            }
            providers.contains(LocationManager.GPS_PROVIDER) -> {
                LocationManager.GPS_PROVIDER
            }
            else -> {
                null
            }
        }
        if (locationProvider != null) {
            locationManager.getLastKnownLocation(locationProvider)?.let {
                tuyaService.getWeather(it.longitude, it.latitude).asLiveData()
                    .observe(viewLifecycleOwner) {
                        binding.layoutWeather.tvTemp.text = it.temp
                        binding.layoutWeather.tvWeather.text = it.condition
                        setTempPic(it.iconUrl)
                    }
            }
        }
    }

    private fun setTempPic(iconUrl: String) {
        val ivWeather = binding.layoutWeather.ivWeather
        if (iconUrl.contains("RAIN") || iconUrl.contains("SHOWERS") || iconUrl.contains("RAIM") || iconUrl.contains("SLEET")) {
            ivWeather.setImageResource(R.drawable.ic_weather_rain)
        } else if (iconUrl.contains("SUNNY") || iconUrl.contains("CLEAR")) {
            ivWeather.setImageResource(R.drawable.ic_weather_sun)
        } else if (iconUrl.contains("BLIZZARD") || iconUrl.contains("SNOW") || iconUrl.contains("HAIL") || iconUrl.contains("ICY") || iconUrl.contains(
                "ICE"
            ) || iconUrl.contains("SNOW_SHOWERS")
        ) {
            ivWeather.setImageResource(R.drawable.ic_weather_snow)
        } else if (iconUrl.contains("CLOUDY") || iconUrl.contains("OVERCAST")) {
            ivWeather.setImageResource(R.drawable.ic_weather_cloudy)
        } else if (iconUrl.contains("DUST") || iconUrl.contains("FOG") || iconUrl.contains("HAZE") || iconUrl.contains("SAND") || iconUrl.contains("SANDSTORM")) {
            ivWeather.setImageResource(R.drawable.ic_weather_sandstorm)
        } else if (iconUrl.contains("LIGHTNING") || iconUrl.contains("THUNDERSHOWER") || iconUrl.contains("THUNDERSTORM")) {
            ivWeather.setImageResource(R.drawable.ic_weather_rainstorm)
        } else {
            ivWeather.setImageResource(R.drawable.ic_weather_null)
        }
    }
}