package com.anyer.hdp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.anyer.hdp.models.BleDevice
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class Bluetooth(private val context: Context) {
    companion object {
        val HEART_RATE_SERVICE = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")
        val HEART_RATE_MEASUREMENT_CHARACTERISTIC =
            UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")
        const val SCAN_TIMEOUT = 1_000L
    }

    @InternalCoroutinesApi
    fun scanForDevices() = flow {
        val devices = mutableSetOf<BleDevice>()
        val adapter = BluetoothAdapter.getDefaultAdapter()

        if (!adapter.isEnabled) {
            throw Exception("Bluetooth adapter is disabled")
        }

        val uuid = ParcelUuid(HEART_RATE_SERVICE)
        val filter = ScanFilter.Builder().setServiceUuid(uuid).build()
        val filters = listOf(filter)

        val settings = ScanSettings
            .Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.let { scanResult ->
                    devices.add(bluetoothDeviceToModel(scanResult.device))
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                results?.let { scanResults ->
                    devices.addAll(scanResults.map { scanResult ->
                        bluetoothDeviceToModel(scanResult.device)
                    })
                }
            }

            fun bluetoothDeviceToModel(device: BluetoothDevice) =
                BleDevice(device.address, device.name)
        }

        while (isActive) {
            startScan(adapter, filters, settings, scanCallback)
            delay(SCAN_TIMEOUT)
            stopScan(adapter, scanCallback)
            emit(devices)
        }
    }

    private fun stopScan(
        adapter: BluetoothAdapter,
        scanCallback: ScanCallback
    ) {
        context.runWithPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            adapter.bluetoothLeScanner.stopScan(scanCallback)
        }
    }

    private fun startScan(
        adapter: BluetoothAdapter,
        filters: List<ScanFilter>,
        settings: ScanSettings?,
        scanCallback: ScanCallback
    ) {
        context.runWithPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            adapter.bluetoothLeScanner.startScan(filters, settings, scanCallback)
        }
    }
}