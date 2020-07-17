package com.anyer.hdp.ui.devices

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
    private val devicesAdapter = DevicesAdapter()
    private val viewModel by lazy {
        (activity as MainActivity).appViewModel
    }
    private lateinit var binding: FragmentDevicesBluetoothBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDevicesBluetoothBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        loadAllDevices()

        setupScanSwitch()

        setupScanProgress()
    }

    private fun setupScanProgress() {
        val max = 15
        binding.progressBar.max = max
        viewModel.scanProgress.observe(viewLifecycleOwner, Observer {
            binding.scanProgress = it

            if (it >= max) {
                binding.scan.isChecked = false
                binding.scanProgress = -1
                viewModel.stopScanDevices()
            }
        })
    }

    private fun setupScanSwitch() {
        binding.scan.isChecked = false
        binding.scan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.startScanDevices()
            } else {
                viewModel.stopScanDevices()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewDevices.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewDevices.adapter = devicesAdapter
    }

    private fun loadAllDevices() {
        viewModel.allDevices().observe(viewLifecycleOwner, Observer { devices ->
            devicesAdapter.submitList(devices)
        })
    }
}