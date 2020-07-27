package com.anyer.hdp.ui.devices

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.anyer.hdp.models.BleDevice
import kotlinx.android.synthetic.main.device.view.*

class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindTo(device: BleDevice, clickListener: View.OnClickListener) {
        itemView.textViewDeviceName.text = device.name
        itemView.heartRate.text = device.heartRate.toString()
        itemView.heartRate.visibility = if (device.heartRate != 0) View.VISIBLE else View.GONE
        itemView.imageView.setColorFilter(if (device.heartRate != 0) Color.RED else Color.GRAY)
        itemView.bodySensorLocation.text = device.bodySensorLocationString
        itemView.cardview.setOnClickListener {
            clickListener.onClick(itemView)
        }
    }
}
