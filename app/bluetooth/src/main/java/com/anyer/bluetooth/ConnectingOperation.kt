package com.anyer.bluetooth

import android.bluetooth.BluetoothGattCharacteristic

/**
 * A simple wrapper to provide a more semantic [Operation]
 * for connecting to a remote device.
 */
class ConnectingOperation : Operation {
    override fun execute() {
        // Do Nothing
    }

    override fun complete(result: Result<BluetoothGattCharacteristic?>) {
        // Do Nothing
    }
}
