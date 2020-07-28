package com.anyer.hdp.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anyer.hdp.bluetooth.BleScanCallback
import com.anyer.hdp.bluetooth.bluetoothStartScanDevices
import com.anyer.hdp.bluetooth.bluetoothStopScanDevices
import com.anyer.hdp.databinding.FragmentDevicesBluetoothBinding
import com.anyer.hdp.models.BleDevice
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class DevicesFragment : Fragment(), View.OnClickListener {
    private val viewModel: DevicesViewModel by viewModels()

    @Inject
    lateinit var devicesAdapter: DevicesAdapter

    private lateinit var binding: FragmentDevicesBluetoothBinding

    private val bleScanCallback = BleScanCallback { devices ->
        viewModel.onBluetoothScanChanged(devices)
    }

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

        observeDevices()
        observeScanning()
    }

    override fun onClick(v: View?) {

    }

    private fun setupScanSwitch() {
        binding.scan.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onSwitchChanged(isChecked)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewDevices.layoutManager = LinearLayoutManager(context)

        devicesAdapter.onClickListener = object : DeviceClickListener {
            override fun onDeviceClick(device: BleDevice) {
                val action =
                    DevicesFragmentDirections.actionDevicesToDetails(device.address)
                findNavController().navigate(action)
            }
        }
        binding.recyclerViewDevices.adapter = devicesAdapter
    }

    private fun observeDevices() {
        viewModel.devices.observe(viewLifecycleOwner, Observer {
            devicesAdapter.submitList(it)
        })
    }

    private fun observeScanning() {
        viewModel.scanning.observe(viewLifecycleOwner, Observer { scanning ->
            if (scanning) {
                bluetoothStartScanDevices(this, bleScanCallback)
            } else {
                bluetoothStopScanDevices(this, bleScanCallback)
            }
        })
    }
}