package com.anyer.hdp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.anyer.hdp.dao.DevicesDao
import com.anyer.hdp.db.entities.Device
import com.anyer.hdp.models.BleDevice
import kotlinx.coroutines.Dispatchers

class AppRepository(private val devicesDao: DevicesDao) {
    fun allDevices(): LiveData<List<BleDevice>> = liveData(Dispatchers.IO) {
        emitSource(
            devicesDao
                .getDevices()
                .map { devices ->
                    devices.map { device ->
                        BleDevice(
                            device.address,
                            device.name,
                            device.heartRate,
                            device.bodySensorLocation,
                            device.connected
                        )
                    }
                }
        )
    }

    fun updateDevices(devices: Set<BleDevice>) {
        devicesDao.insertDevices(devices.map { device ->
            Device(
                device.address,
                device.name,
                device.heartRate,
                device.bodySensorLocation,
                device.connected
            )
        })
    }

    fun updateHeartRate(address: String, value: Int) {
        devicesDao.updateHeartRate(address, value)
    }

    fun updateBodySensorLocation(address: String, value: Int) {
        devicesDao.updateBodySensorLocation(address, value)
    }

    fun updateConnected(address: String, connected: Boolean) {
        devicesDao.updateConnected(address, connected)
    }
}