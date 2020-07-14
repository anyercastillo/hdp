package com.anyer.hdp

import android.bluetooth.le.ScanCallback

open class BluetoothScanCallback : ScanCallback() {
    var stopScan: () -> Unit = {}
        private set

    fun onStopScan(function: () -> Unit) {
        stopScan = function
    }
}