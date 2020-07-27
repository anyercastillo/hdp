package com.anyer.hdp.ui.devices

import com.anyer.hdp.models.BleDevice

interface DeviceClickListener {
    fun onDeviceClick(device: BleDevice)
}