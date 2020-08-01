package com.anyer.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import com.android.example.github.testing.OpenForTesting

@OpenForTesting
open class CharacteristicSubscription {
    private var _onValueChange: ((result: Result<BluetoothGattCharacteristic?>) -> Unit)? = null

    /**
     * Called every time a [BluetoothGattCharacteristic] arrived as a notification.
     */
    fun onValueChange(callback: (result: Result<BluetoothGattCharacteristic?>) -> Unit) {
        _onValueChange = callback
    }

    @OpenForTesting
    open fun notify(result: Result<BluetoothGattCharacteristic?>) {
        _onValueChange?.let { it(result) }
    }
}