package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.android.example.github.testing.OpenForTesting

@OpenForTesting
open class CharacteristicSubscription {
    private var _gatt: BluetoothGatt? = null
    private var _onValueChange: ((BluetoothGattCharacteristic) -> Unit)? = null

    /**
     * Called every time a [BluetoothGattCharacteristic] arrived as a notification.
     */
    fun onValueChange(callback: (BluetoothGattCharacteristic) -> Unit) {
        _onValueChange = callback
    }

    @OpenForTesting
    open fun notify(
        gatt: BluetoothGatt?,
        bluetoothGattCharacteristic: BluetoothGattCharacteristic
    ) {
        _gatt = gatt
        _onValueChange?.let { it(bluetoothGattCharacteristic) }
    }

    /**
     * Disconnect from [BluetoothGatt]
     */
    fun unsubscribe() {
        _gatt?.disconnect()
    }
}