package com.anyer.hdp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.anyer.hdp.db.AppRoomDatabase
import com.anyer.hdp.db.entities.Device
import com.anyer.hdp.models.BleDevice
import javax.inject.Inject

class AppRepository @Inject constructor(db: AppRoomDatabase) {
    private val devicesDao = db.devicesDao()

    fun allDevices(): LiveData<List<BleDevice>> = devicesDao
        .getDevices()
        .map { devices -> devices.map { device -> BleDevice.fromEntity(device) } }

    fun updateDevices(devices: Set<BleDevice>) {
        devicesDao.insertDevices(devices.map { device -> Device.fromModel(device) })
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

    fun getDevice(address: String) = devicesDao.getDevice(address).map { BleDevice.fromEntity(it) }
}