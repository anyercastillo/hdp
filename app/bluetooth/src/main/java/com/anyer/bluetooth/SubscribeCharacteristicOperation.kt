package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.util.Log
import java.util.*

class SubscribeCharacteristicOperation(
    private val bluetoothGatt: BluetoothGatt,
    private val service: UUID,
    private val characteristic: UUID,
    private val onComplete: (result: Result<BluetoothGattCharacteristic?>) -> Unit
) : Operation {
    companion object {
        val DESCRIPTOR_NOTIFICATION_UUID: UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    override fun execute() {
        val gattCharacteristic =
            bluetoothGatt.getService(service)?.getCharacteristic(characteristic)

        bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true)

        // A better API would be for setCharacteristicNotification to set the descriptor,
        // but unfortunately it doesn't seem to work that way.
        val descriptor = gattCharacteristic?.getDescriptor(
            DESCRIPTOR_NOTIFICATION_UUID
        )

        if (descriptor == null) {
            complete(Result.failure(Exception("Descriptor not found")))

            return
        }

        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        bluetoothGatt.writeDescriptor(descriptor)
    }

    override fun complete(result: Result<BluetoothGattCharacteristic?>) {
        onComplete(result)
    }
}