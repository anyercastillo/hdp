package com.anyer.hdp.ui.devices

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anyer.hdp.databinding.FragmentDevicesBluetoothBinding
import com.anyer.hdp.ui.MainActivity


/**
 * A simple [Fragment] subclass.
 */
class DevicesFragment : Fragment() {
    private val devicesAdapter = DevicesAdapter { device ->
        BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.address)
            .connectGatt(context, false, gattCallback)
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
            (activity as MainActivity).appViewModel.allDevices(it).observe(
                viewLifecycleOwner,
                Observer { devices ->
                    devicesAdapter.submitList(devices)
                })
        }
    }
}