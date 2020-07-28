package com.anyer.hdp.models

import com.anyer.hdp.db.entities.Device

data class BleDevice(
    val address: String,
    val name: String,
    val heartRate: Int,
    val bodySensorLocation: Int,
    val connected: Boolean
) {
    companion object {
        fun fromEntity(device: Device) = BleDevice(
            device.address,
            device.name,
            device.heartRate,
            device.bodySensorLocation,
            device.connected
        )
    }

    val bodySensorLocationString: String
        get() = when (bodySensorLocation) {
            1 -> "Chest"
            2 -> "Wrist"
            3 -> "Finger"
            4 -> "Hand"
            5 -> "Ear Lobe"
            6 -> "Foot"

            else -> "Other"
        }
}