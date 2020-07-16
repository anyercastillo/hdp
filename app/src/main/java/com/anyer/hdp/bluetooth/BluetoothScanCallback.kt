package com.anyer.hdp.bluetooth

import android.bluetooth.le.ScanCallback

open class BluetoothScanCallback : ScanCallback() {
    var stopScan: () -> Unit = {}
        private set

    fun onStopScan(function: () -> Unit) {
        stopScan = function
    }
}