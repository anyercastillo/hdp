package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import java.util.*

class ReadCharacteristicOperation(
    private val bluetoothGatt: BluetoothGatt,
    private val service: UUID,
    private val characteristic: UUID,
    private val onComplete: (result: Result<BluetoothGattCharacteristic?>) -> Unit
) : Operation {
    override fun execute() {
        val gattCharacteristic =
            bluetoothGatt.getService(service)?.getCharacteristic(characteristic)

        bluetoothGatt.readCharacteristic(gattCharacteristic)
    }

    override fun complete(result: Result<BluetoothGattCharacteristic?>) {
        onComplete(result)
    }
}