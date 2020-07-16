package com.anyer.hdp.ui.devices

import androidx.recyclerview.widget.DiffUtil
import com.anyer.hdp.models.BleDevice

class DeviceItemDiffCallback : DiffUtil.ItemCallback<BleDevice>() {
    override fun areItemsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: BleDevice, newItem: BleDevice): Boolean =
        oldItem == newItem
}