package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.android.example.github.testing.OpenForTesting
import java.util.*
import kotlin.coroutines.Continuation

@OpenForTesting
open class SubscribeCharacteristicCallback(
    private val service: UUID,
    private val characteristic: UUID,
    val subscription: CharacteristicSubscription
) : BluetoothGattCallback() {
    private var _continuation: Continuation<Unit>? = null

    @OpenForTesting
    open fun setContinuation(continuation: Continuation<Unit>) {
        _continuation = continuation
    }

    companion object {
        val DESCRIPTOR_NOTIFICATION_UUID: UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
            gatt?.requestMtu(256)
        }
    }

    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        gatt?.discoverServices()
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (gatt == null || status == BluetoothGatt.GATT_FAILURE) {
            _continuation?.resumeWith(Result.failure(CharacteristicSubscribeException("Service Discover fails")))

            return
        }

        val bluetoothGattService = gatt.getService(service)
        val bluetoothGattCharacteristic = bluetoothGattService
            ?.getCharacteristic(characteristic)

        gatt.setCharacteristicNotification(bluetoothGattCharacteristic, true)

        // A better API would be for setCharacteristicNotification to set the descriptor,
        // but unfortunately it doesn't seem to work that way.
        val descriptor = bluetoothGattCharacteristic?.getDescriptor(DESCRIPTOR_NOTIFICATION_UUID)

        if (descriptor == null) {
            _continuation?.resumeWith(Result.failure(CharacteristicSubscribeException("Descriptor not found")))

            return
        }

        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt.writeDescriptor(descriptor)

        _continuation?.resumeWith(Result.success(Unit))
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        bluetoothGattCharacteristic: BluetoothGattCharacteristic?
    ) {
        if (bluetoothGattCharacteristic != null) {
            subscription.notify(gatt, bluetoothGattCharacteristic)
        }
    }
}
