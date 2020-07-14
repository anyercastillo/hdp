package com.anyer.hdp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import java.util.*

class Bluetooth {
    fun scanForDevices(context: Context, scanCallback: BluetoothScanCallback) {
        val adapter = BluetoothAdapter.getDefaultAdapter()

        if (!adapter.isEnabled) {
            return scanCallback.onScanFailed(-1)
        }

        scanCallback.onStopScan {
            context.runWithPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) {
                adapter.bluetoothLeScanner.stopScan(scanCallback)
            }
        }

        val serviceUUID = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")
        val uuid = ParcelUuid(serviceUUID)
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