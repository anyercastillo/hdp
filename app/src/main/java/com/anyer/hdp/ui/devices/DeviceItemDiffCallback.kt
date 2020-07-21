package com.anyer.hdp.ui.devices

import androidx.recyclerview.widget.DiffUtil
import com.anyer.hdp.models.BleDevice
import javax.inject.Inject

class DeviceItemDiffCallback @Inject constructor(): DiffUtil.ItemCallback<BleDevice>() {
    override fun areItemsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean =
        oldItem.name == newItem.name && oldItem.heartRate == newItem.heartRate
}