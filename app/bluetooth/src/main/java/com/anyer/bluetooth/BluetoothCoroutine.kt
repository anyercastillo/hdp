package com.anyer.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

/**
 * Reads a characteristic and disconnect from the given @remoteDevice.
 *
 * @param remoteDevice A [BluetoothDevice]
 * @param callback A [ReadCharacteristicCallback]
 * @return a BluetoothGattCharacteristic
 *
 * Usage:
 * val bluetoothGattCharacteristic = readCharacteristicCoroutine(remoteDevice, callback)
 */
suspend fun readCharacteristicCoroutine(
    remoteDevice: BluetoothDevice,
    callback: ReadCharacteristicCallback
) =
    withContext(Dispatchers.IO) {
        var connection: BluetoothGatt? = null
        val bluetoothGattCharacteristic =
            suspendCoroutine { continuation: Continuation<BluetoothGattCharacteristic> ->
                callback.setContinuation(continuation)

                connection = remoteDevice.connectGatt(null, false, callback)
            }

        connection?.disconnect()

        bluetoothGattCharacteristic
    }

/**
 * This coroutine takes care of connect to the given @address and resolves the @characteristic
 * from the @service and then it subscribes itself. The returned subscription can be used to observe
 * new values. It is responsibility of the caller to unsubscribe.
 *
 * @param remoteDevice A [BluetoothDevice]
 * @param callback A [SubscribeCharacteristicCallback]
 * @return A [CharacteristicSubscription] instance.
 *
 * Usage:
 * val subscription = subscribeToCharacteristicCoroutine(device, service, characteristic)
 * subscription.onValueChange { bluetoothGattCharacteristic: BluetoothGattCharacteristic -> ....}
 * subscription.unsubscribe()
 */
suspend fun subscribeToCharacteristicCoroutine(
    remoteDevice: BluetoothDevice,
    callback: SubscribeCharacteristicCallback
) = withContext(Dispatchers.IO) {
    suspendCoroutine { continuation: Continuation<Unit> ->
        callback.setContinuation(continuation)

        remoteDevice.connectGatt(null, false, callback)
    }

    callback.subscription
}
