package com.anyer.hdp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anyer.hdp.models.BleDevice

@Entity(tableName = "devices")
data class Device(
    val address: String,
    @PrimaryKey val name: String,
    val heartRate: Int,
    val bodySensorLocation: Int,
    val connected: Boolean
) {
    companion object {
        fun fromModel(bleDevice: BleDevice) = Device(
            bleDevice.address,
            bleDevice.name,
            bleDevice.heartRate,
            bleDevice.bodySensorLocation,
            bleDevice.connected
        )
    }
}