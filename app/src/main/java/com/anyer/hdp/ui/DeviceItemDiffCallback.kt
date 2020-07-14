package com.anyer.hdp.ui

import android.bluetooth.BluetoothDevice
import androidx.recyclerview.widget.DiffUtil

class DeviceItemDiffCallback : DiffUtil.ItemCallback<BluetoothDevice>() {
    override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean =
        oldItem == newItem

}