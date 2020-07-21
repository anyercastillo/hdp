package com.anyer.hdp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import androidx.fragment.app.Fragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import java.util.*

val HEART_RATE_SERVICE: UUID = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")

val HEART_RATE_MEASUREMENT_CHARACTERISTIC: UUID =
    UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")

val HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC: UUID =
    UUID.fromString("00002A38-0000-1000-8000-00805F9B34FB")

/**
 * Receiving context as a <androidx.fragment.app.Fragment> is a decent workaround to the constraint
 * added by <androidx.hilt>
 *
 * The issue was created on the <QuickPermissions-Kotlin> project
 * https://github.com/QuickPermissions/QuickPermissions-Kotlin/issues/20
 */
fun bluetoothStartScanDevices(context: Fragment, bleScanCallback: BleScanCallback) {
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

fun bluetoothStopScanDevices(context: Fragment, bleScanCallback: BleScanCallback) {
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


private fun runWithPermissions(context: Fragment, callback: () -> Unit) = context.runWithPermissions(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
) {
    callback()
}
