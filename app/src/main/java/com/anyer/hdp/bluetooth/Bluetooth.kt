package com.anyer.hdp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

class Bluetooth(private val context: Context) {
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val connectedGattMap = mutableMapOf<String, BluetoothGatt>()

    fun startScanDevices(bleScanCallback: BleScanCallback) {
        val uuid = ParcelUuid(HEART_RATE_SERVICE)
        val filter = ScanFilter.Builder().setServiceUuid(uuid).build()
        val filters = listOf(filter)

        val settings = ScanSettings
            .Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        runWithPermissions {
            adapter.bluetoothLeScanner.startScan(filters, settings, bleScanCallback)
        }
    }

    fun stopScanDevices(bleScanCallback: BleScanCallback) {
        runWithPermissions {
            adapter.bluetoothLeScanner.stopScan(bleScanCallback)
        }
    }

    fun connectGatt(address: String, heartRateGattCallback: HeartRateGattCallback) {
        connectedGattMap[address]?.disconnect()

        val connection = adapter
            .getRemoteDevice(address)
            .connectGatt(context, false, heartRateGattCallback)

        connectedGattMap[address] = connection
    }

    private fun runWithPermissions(callback: () -> Unit) = context.runWithPermissions(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) {
        callback()
    }
}