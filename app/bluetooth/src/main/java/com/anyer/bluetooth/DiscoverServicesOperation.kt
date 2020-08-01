package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic

class DiscoverServicesOperation(private val bluetoothGatt: BluetoothGatt) : Operation {
    override fun execute() {
        bluetoothGatt.discoverServices()
    }

    override fun complete(result: Result<BluetoothGattCharacteristic?>) {
        // Do Nothing!
    }
}