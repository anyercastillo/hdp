package com.anyer.hdp.models

data class BleDevice(
    val address: String,
    val name: String,
    val heartRate: Int,
    val bodySensorLocation: Int,
    val connected: Boolean
) {
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