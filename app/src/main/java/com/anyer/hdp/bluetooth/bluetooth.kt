package com.anyer.hdp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import java.util.*

val HEART_RATE_SERVICE: UUID = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")

val HEART_RATE_MEASUREMENT_CHARACTERISTIC: UUID =
    UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")

val HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC: UUID =
    UUID.fromString("00002A38-0000-1000-8000-00805F9B34FB")

fun bluetoothStartScanDevices(context: Context, bleScanCallback: BleScanCallback) {
    val uuid = ParcelUuid(HEART_RATE_SERVICE)
    val filter = ScanFilter.Builder().setServiceUuid(uuid).build()
    val filters = listOf(filter)

    val settings = ScanSettings
        .Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    runWithPermissions(context) {
        BluetoothAdapter
            .getDefaultAdapter()
            .bluetoothLeScanner
            .startScan(filters, settings, bleScanCallback)
    }
}

fun bluetoothStopScanDevices(context: Context, bleScanCallback: BleScanCallback) {
    runWithPermissions(context) {
        BluetoothAdapter
            .getDefaultAdapter()
            .bluetoothLeScanner
            .stopScan(bleScanCallback)
    }
}

fun bluetoothConnectGatt(
    address: String,
    context: Context,
    heartRateGattCallback: HeartRateGattCallback
): BluetoothGatt = BluetoothAdapter
    .getDefaultAdapter()
    .getRemoteDevice(address)
    .connectGatt(context, false, heartRateGattCallback)


private fun runWithPermissions(context: Context, callback: () -> Unit) = context.runWithPermissions(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
) {
    callback()
}
