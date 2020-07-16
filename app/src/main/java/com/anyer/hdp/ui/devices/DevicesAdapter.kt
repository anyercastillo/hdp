package com.anyer.hdp.ui.devices

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.anyer.hdp.R

class DevicesAdapter(private val onDeviceClicked: (device: BluetoothDevice) -> Unit = {}) :
    ListAdapter<BluetoothDevice, DeviceViewHolder>(DeviceItemDiffCallback()) {
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