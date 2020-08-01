package com.anyer.bluetooth

import android.bluetooth.BluetoothGattCharacteristic

/**
 * A subscription for characteristic.
 * Subscriber must provide an action using [onValueChange].
 *
 * Example:
 * val subscription = CharacteristicSubscription()
 * subscription.onValueChange { result ->
 *    Log.d("TAG", "Result: $result")
 * }
 *
 * subscription.notify(Result.success(characteristic))
 */
class CharacteristicSubscription {
    private var _onValueChange: ((result: Result<BluetoothGattCharacteristic?>) -> Unit)? = null

    /**
     * Called every time a [BluetoothGattCharacteristic] arrived as a notification.
     */
    fun onValueChange(callback: (result: Result<BluetoothGattCharacteristic?>) -> Unit) {
        _onValueChange = callback
    }

    fun notify(result: Result<BluetoothGattCharacteristic?>) {
        _onValueChange?.let { it(result) }
    }
}