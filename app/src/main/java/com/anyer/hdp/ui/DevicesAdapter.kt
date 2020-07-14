package com.anyer.hdp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.anyer.hdp.R
import com.anyer.hdp.models.Device

class DevicesAdapter(private val onDeviceClicked: (device: Device) -> Unit = {}) :
    ListAdapter<Device, DeviceViewHolder>(DeviceItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.device, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bindTo(getItem(position), onDeviceClicked)
    }
}