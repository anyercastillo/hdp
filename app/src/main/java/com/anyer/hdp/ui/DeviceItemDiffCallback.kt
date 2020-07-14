package com.anyer.hdp.ui

import androidx.recyclerview.widget.DiffUtil
import com.anyer.hdp.models.Device

class DeviceItemDiffCallback : DiffUtil.ItemCallback<Device>() {
    override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean = oldItem == newItem

}