package com.anyer.hdp.ui.devices

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anyer.hdp.models.BleDevice
import kotlinx.android.synthetic.main.device.view.*

class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindTo(
        device: BleDevice,
        onDeviceClicked: (device: BleDevice) -> Unit
    ) {
        itemView.textViewDeviceName.text = device.name
        itemView.setOnClickListener {
            onDeviceClicked(device)
        }
    }
}
