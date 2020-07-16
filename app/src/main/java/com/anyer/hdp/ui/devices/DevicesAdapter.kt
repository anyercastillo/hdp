package com.anyer.hdp.ui.devices

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.anyer.hdp.R
import com.anyer.hdp.models.BleDevice

class DevicesAdapter : ListAdapter<BleDevice, DeviceViewHolder>(DeviceItemDiffCallback()) {
    var deviceClickedCallback: (device: BleDevice) -> Unit = {}
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.device, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bindTo(getItem(position), deviceClickedCallback)
    }

    fun onDeviceClicked(callback: (device: BleDevice) -> Unit) {
        deviceClickedCallback = callback
    }
}