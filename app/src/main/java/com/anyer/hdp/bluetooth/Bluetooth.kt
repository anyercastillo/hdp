package com.anyer.hdp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import com.anyer.hdp.models.BleDevice
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.*

class Bluetooth(private val context: Context) {
    private var scanning = false
    private val leScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    private val devices = mutableSetOf<BleDevice>()
    private val scanCallback = object : ScanCallback() {
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

    companion object {
        val HEART_RATE_SERVICE = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")
        val HEART_RATE_MEASUREMENT_CHARACTERISTIC =
            UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")
        const val SCAN_COLLECT_TIMEOUT = 300L
    }

    fun startScanDevices() = flow {
        val uuid = ParcelUuid(HEART_RATE_SERVICE)
        val filter = ScanFilter.Builder().setServiceUuid(uuid).build()
        val filters = listOf(filter)

        val settings = ScanSettings
            .Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanning = true
        startScan(leScanner, filters, settings, scanCallback)

        while (scanning) {
            delay(SCAN_COLLECT_TIMEOUT)
            emit(devices)
        }
    }

    fun stopScanDevices() {
        scanning = false
        stopScan(leScanner, scanCallback)
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