package com.anyer.hdp.ui.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anyer.hdp.bluetooth.*
import com.anyer.hdp.databinding.FragmentDevicesBluetoothBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class DevicesFragment : Fragment() {
    private val viewModel: DevicesViewModel by viewModels()

    @Inject
    lateinit var devicesAdapter: DevicesAdapter

    private lateinit var binding: FragmentDevicesBluetoothBinding

    private val bleScanCallback = BleScanCallback { devices ->
        viewModel.onBluetoothScanChanged(devices)
    }

    private val connectionManager by lazy {
        ConnectionManager(requireContext(), object : HeartRateGattCallbackFactory {
            override fun create(address: String): HeartRateGattCallback {
                val onValueChanged = { characteristic: UUID, value: Int ->
                    viewModel.onCharacteristicChanged(address, characteristic, value)
                }

                return HeartRateGattCallback(onValueChanged)
            }
        })
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

        viewLifecycleOwner.lifecycle.addObserver(connectionManager)

        setupRecyclerView()
        setupScanSwitch()

        observeDevices()
        observeScanning()
        observeConnectAddresses()
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

    private fun observeConnectAddresses() {
        viewModel.connectAddresses.observe(viewLifecycleOwner, Observer { addresses ->
            connectionManager.updateConnections(addresses)
        })
    }
}