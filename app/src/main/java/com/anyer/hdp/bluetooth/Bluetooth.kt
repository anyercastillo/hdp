package com.anyer.hdp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import androidx.lifecycle.liveData
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.coroutines.delay

class Bluetooth(private val context: Context) {
    private var scanning = false
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val connectedGatt = mutableSetOf<String>()

    fun startScanDevices(bleScanCallback: BleScanCallback) {
        val uuid = ParcelUuid(HEART_RATE_SERVICE)
        val filter = ScanFilter.Builder().setServiceUuid(uuid).build()
        val filters = listOf(filter)

        val settings = ScanSettings
            .Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanning = true
        startScan(adapter.bluetoothLeScanner, filters, settings, bleScanCallback)
    }

    fun stopScanDevices(bleScanCallback: BleScanCallback) {
        scanning = false
        stopScan(adapter.bluetoothLeScanner, bleScanCallback)
    }

    fun scanProgress() = liveData {
        var progress = 0

        while (true) {
            if (scanning) {
                progress++
            } else {
                progress = 0
            }

            emit(progress)

            delay(1000L)
        }
    }

    fun connectGatt(address: String, heartRateGattCallback: HeartRateGattCallback) {
        if (connectedGatt.contains(address)) return

        adapter
            .getRemoteDevice(address)
            .connectGatt(context, false, heartRateGattCallback)

        connectedGatt.add(address)
    }

    private fun stopScan(
        leScanner: BluetoothLeScanner,
        scanCallback: ScanCallback
    ) {
        context.runWithPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            leScanner.stopScan(scanCallback)
        }
    }

    private fun startScan(
        leScanner: BluetoothLeScanner,
        filters: List<ScanFilter>,
        settings: ScanSettings?,
        scanCallback: ScanCallback
    ) {
        context.runWithPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            leScanner.startScan(filters, settings, scanCallback)
        }
    }
}