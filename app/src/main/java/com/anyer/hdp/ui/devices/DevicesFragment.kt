package com.anyer.hdp.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anyer.hdp.databinding.FragmentDevicesBluetoothBinding


/**
 * A simple [Fragment] subclass.
 */
class DevicesFragment : Fragment() {
    private val viewModel by viewModels<DevicesViewModel> {
        DevicesViewModelFactory(requireContext())
    }
    private val devicesAdapter = DevicesAdapter()
    private lateinit var binding: FragmentDevicesBluetoothBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDevicesBluetoothBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupScanSwitch()
        loadAllDevices()
    }

    private fun setupScanSwitch() {
        binding.scan.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSwitchChanged(isChecked)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewDevices.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDevices.adapter = devicesAdapter
    }

    private fun loadAllDevices() {
        viewModel.allDevices().observe(viewLifecycleOwner, Observer { devices ->
            devicesAdapter.submitList(devices)

            devices.forEach { device ->
                viewModel.connectGatt(device.address)
            }
        })
    }
}