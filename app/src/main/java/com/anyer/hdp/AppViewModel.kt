package com.anyer.hdp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anyer.hdp.bluetooth.BleScanCallback
import com.anyer.hdp.bluetooth.Bluetooth
import com.anyer.hdp.bluetooth.HeartRateGattCallback
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AppViewModel(private val repository: AppRepository, private val bluetooth: Bluetooth) :
    ViewModel() {
    val scanProgress: LiveData<Int> = bluetooth.scanProgress()

    fun allDevices(): LiveData<List<BleDevice>> {
        return repository.allDevices()
    }

    fun startScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        repository.removeAllDevices()
        bleScanCallback.restart()
        bluetooth.startScanDevices(bleScanCallback)
    }

    fun stopScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        bluetooth.stopScanDevices(bleScanCallback)
    }

    private fun getHeartRate(address: String) {
        bluetooth.getHeartRate(address, HeartRateGattCallback { value ->
            Log.e("TAG", "getHeartRate: $address $value")
            repository.updateHeartRate(address, value)
        })
    }

    private val bleScanCallback = BleScanCallback { devices ->
        repository.updateDevices(devices.toSet())

        devices.forEach { device ->
            getHeartRate(device.address)
        }
    }
}