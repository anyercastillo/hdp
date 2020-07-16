package com.anyer.hdp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.anyer.hdp.db.entities.Device
import com.anyer.hdp.models.BleDevice

@Dao
interface DevicesDao {
    @Query("SELECT * from devices ORDER BY name ASC")
    fun getDevices(): LiveData<List<Device>>

    @Query("DELETE FROM devices")
    fun removeDevices()

    @Insert
    fun insertDevices(devices: List<Device>)

    @Transaction
    fun updateDevices(devices: Set<BleDevice>) {
        if (devices.isEmpty()) return

        removeDevices()
        insertDevices(devices
            .toList()
            .map { bleDevice -> Device(bleDevice.address, bleDevice.name) }
        )
    }
}