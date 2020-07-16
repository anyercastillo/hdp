package com.anyer.hdp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.anyer.hdp.bluetooth.Bluetooth
import com.anyer.hdp.dao.DevicesDao
import com.anyer.hdp.models.BleDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class AppRepository(private val devicesDao: DevicesDao) {
    @OptIn(InternalCoroutinesApi::class)
    fun allDevices(context: Context): LiveData<List<BleDevice>> = liveData(Dispatchers.IO) {
        emitSource(
            devicesDao
                .getDevices()
                .map { devices ->
                    devices.map { device ->
                        BleDevice(device.address, device.name)
                    }
                }
        )

        val flow = Bluetooth(context).scanForDevices()
        flow.collect {
            devicesDao.updateDevices(it)
        }
    }
}