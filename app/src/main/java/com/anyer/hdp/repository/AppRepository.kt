package com.anyer.hdp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.anyer.hdp.dao.DevicesDao
import com.anyer.hdp.models.BleDevice
import kotlinx.coroutines.Dispatchers

class AppRepository(private val devicesDao: DevicesDao) {
    fun allDevices(): LiveData<List<BleDevice>> = liveData(Dispatchers.IO) {
        emitSource(
            devicesDao
                .getDevices()
                .map { devices ->
                    devices.map { device ->
                        BleDevice(device.address, device.name)
                    }
                }
        )
    }

    fun updateDevices(devices: Set<BleDevice>) {
        devicesDao.updateDevices(devices)
    }
}