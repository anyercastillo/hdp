package com.anyer.hdp.ui.devices

import android.os.CountDownTimer
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DevicesViewModel @ViewModelInject constructor(
    private val repository: AppRepository
) : ViewModel() {
    private val _scanning = MutableLiveData<Boolean>()
    val scanning: LiveData<Boolean> = _scanning

    private val _scanProgress = MutableLiveData<Int>()
    val scanProgress: LiveData<Int> = _scanProgress

    val scanProgressMax = 5 // seconds

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

    val devices: LiveData<List<BleDevice>> = repository.allDevices()

    fun onSwitchChanged(isChecked: Boolean) {
        if (isChecked) {
            startScanDevices()
        } else {
            stopScanDevices()
        }
    }

    fun onBluetoothScanChanged(devices: List<BleDevice>) {
        repository.updateDevices(devices.toSet())
    }

    private fun startScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(true)
        scanProgressCounter.start()
    }

    private fun stopScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(false)
        scanProgressCounter.cancel()
    }
}