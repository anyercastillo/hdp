package com.anyer.hdp.repository

import androidx.lifecycle.map
import com.anyer.hdp.dao.DevicesDao
import com.anyer.hdp.models.BleDevice

class AppRepository(private val devicesDao: DevicesDao) {
    fun allDevices() = devicesDao
        .getDevices()
        .map { devices ->
            devices.map { device ->
                BleDevice(device.name)
            }
        }
}