package com.anyer.hdp.ui.devices

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anyer.hdp.bluetooth.*
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DevicesViewModel(
    private val repository: AppRepository,
    private val bluetooth: Bluetooth
) : ViewModel() {
    private val _scanning = MutableLiveData<Boolean>()
    val scanning: LiveData<Boolean> = _scanning

    private val _scanProgress = MutableLiveData<Int>()
    val scanProgress: LiveData<Int> = _scanProgress

    val scanProgressMax = 3 // seconds

    private val scanProgressCounter: CountDownTimer =
        object : CountDownTimer(scanProgressMax * 1000L, 100L) {
            override fun onFinish() {
                stopScanDevices()
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = millisUntilFinished / 1000L
                val progress = scanProgressMax - secondsUntilFinished
                _scanProgress.postValue(progress.toInt())
            }
        }

    fun allDevices(): LiveData<List<BleDevice>> {
        return repository.allDevices()
    }

    fun connectGatt(address: String) {
        bluetooth.connectGatt(
            address,
            HeartRateGattCallback(viewModelScope) { characteristic, value ->
                when (characteristic) {
                    HEART_RATE_MEASUREMENT_CHARACTERISTIC -> {
                        repository.updateHeartRate(address, value)
                    }

                    HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC -> {
                        repository.updateBodySensorLocation(address, value)
                    }
                }
            })
    }

    fun onSwitchChanged(isChecked: Boolean) {
        if (isChecked) {
            startScanDevices()
        } else {
            stopScanDevices()
        }
    }

    private fun startScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(true)
        scanProgressCounter.start()
        bluetooth.startScanDevices(bleScanCallback)
    }

    private fun stopScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(false)
        scanProgressCounter.cancel()
        bluetooth.stopScanDevices(bleScanCallback)
    }

    private val bleScanCallback = BleScanCallback { devices ->
        repository.updateDevices(devices.toSet())
    }
}