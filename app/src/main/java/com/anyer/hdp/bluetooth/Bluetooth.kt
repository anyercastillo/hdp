package com.anyer.hdp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import java.util.*

class Bluetooth {
    companion object {
        val HEART_RATE_SERVICE = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")
        val HEART_RATE_MEASUREMENT_CHARACTERISTIC = UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")
        const val SCAN_FAILED = -1
    }

    fun scanForDevices(context: Context, scanCallback: BluetoothScanCallback) {
        val adapter = BluetoothAdapter.getDefaultAdapter()

        if (!adapter.isEnabled) {
            return scanCallback.onScanFailed(SCAN_FAILED)
        }

        scanCallback.onStopScan {
            context.runWithPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) {
                adapter.bluetoothLeScanner.stopScan(scanCallback)
            }
        }

        val uuid = ParcelUuid(HEART_RATE_SERVICE)
        val filter = ScanFilter.Builder().setServiceUuid(uuid).build()
        val filters = listOf(filter)

        val settings = ScanSettings
            .Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        context.runWithPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            adapter.bluetoothLeScanner.startScan(filters, settings, scanCallback)
        }
    }
}