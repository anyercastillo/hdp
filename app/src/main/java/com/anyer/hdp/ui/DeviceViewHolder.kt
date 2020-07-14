package com.anyer.hdp.ui

import android.bluetooth.BluetoothDevice
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anyer.hdp.models.Device
import kotlinx.android.synthetic.main.device.view.*

class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindTo(
        device: BluetoothDevice,
        onDeviceClicked: (device: BluetoothDevice) -> Unit
    ) {
        itemView.textViewDeviceName.text = device.name
        itemView.setOnClickListener {
            onDeviceClicked(device)
        }
    }
}
