package com.anyer.hdp.ui.devices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.anyer.hdp.R
import com.anyer.hdp.models.BleDevice
import javax.inject.Inject

class DevicesAdapter @Inject constructor(diffCallback: DeviceItemDiffCallback) :
    ListAdapter<BleDevice, DeviceViewHolder>(diffCallback) {

    var onClickListener: DeviceClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.device, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = getItem(position)
        holder.bindTo(device, View.OnClickListener {
            onClickListener?.onDeviceClick(device)
        })
    }
}