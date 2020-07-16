package com.anyer.hdp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anyer.hdp.bluetooth.Bluetooth
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AppViewModel(private val repository: AppRepository, private val bluetooth: Bluetooth) :
    ViewModel() {
    val scanProgress: LiveData<Int> = bluetooth.scanProgress()

    fun allDevices(): LiveData<List<BleDevice>> {
        return repository.allDevices()
    }

    fun startScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        val flow = bluetooth.startScanDevices()
        flow.collect { devices ->
            repository.updateDevices(devices)
        }
    }

    fun stopScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        bluetooth.stopScanDevices()
    }
}