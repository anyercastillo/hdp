package com.anyer.hdp.ui

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import androidx.navigation.fragment.findNavController
import com.anyer.hdp.BluetoothScanCallback
import com.anyer.hdp.R
import com.anyer.hdp.models.Device

val DevicesFragment.bluetoothScanCallback: BluetoothScanCallback
    get() = object : BluetoothScanCallback() {
        val devices = mutableListOf<Device>()

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result == null) return

            devicesFound(listOf(result.device))
        }

        override fun onScanFailed(errorCode: Int) {
            findNavController().navigate(R.id.disabledBluetoothFragment)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            if (results == null) return

            devicesFound(results.map { it.device })
        }

        fun devicesFound(results: List<BluetoothDevice>) {
            devices.addAll(results.map { Device(it.name) })
            devicesAdapter.submitList(devices)

            stopScan()
        }
    }