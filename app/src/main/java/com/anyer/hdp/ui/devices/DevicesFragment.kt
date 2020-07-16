package com.anyer.hdp.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anyer.hdp.Bluetooth
import com.anyer.hdp.databinding.FragmentDevicesBluetoothBinding


/**
 * A simple [Fragment] subclass.
 */
class DevicesFragment : Fragment() {
    val devicesAdapter = DevicesAdapter { device ->
        device.connectGatt(context, false, gattCallback)
    }

    private lateinit var binding: FragmentDevicesBluetoothBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDevicesBluetoothBinding.inflate(inflater, container, false)
        binding.recyclerViewDevices.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDevices.adapter = devicesAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            Bluetooth().scanForDevices(it, bluetoothScanCallback)
        }
    }
}