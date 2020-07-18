package com.anyer.hdp.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import com.anyer.hdp.models.BleDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BleScanCallback(
    private val onDevicesFound: (devices: List<BleDevice>) -> Unit
) : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        if (result == null) return

        onDevicesFound(listOf(result))
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        if (results == null) return

        onDevicesFound(results)
    }

    private fun onDevicesFound(results: List<ScanResult>) {
        CoroutineScope(Dispatchers.IO).launch {
            val devices = results.map { scanResult -> bluetoothDeviceToModel(scanResult.device) }

            onDevicesFound(devices)
        }
    }

    private fun bluetoothDeviceToModel(device: BluetoothDevice) =
        BleDevice(device.address, device.name, 0, 0, false)
}