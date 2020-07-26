package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import com.android.example.github.testing.OpenForTesting
import java.util.*
import kotlin.coroutines.Continuation

@OpenForTesting
open class ReadCharacteristicCallback(
    private val service: UUID,
    private val characteristic: UUID
) : BluetoothGattCallback() {
    private var _continuation: Continuation<BluetoothGattCharacteristic>? = null

    @OpenForTesting
    open fun setContinuation(continuation: Continuation<BluetoothGattCharacteristic>) {
        _continuation = continuation
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
            gatt?.discoverServices()
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        val bluetoothGattCharacteristic =
            gatt?.getService(service)?.getCharacteristic(characteristic)

        gatt?.readCharacteristic(bluetoothGattCharacteristic)
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        bluetoothGattCharacteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        if (status == BluetoothGatt.GATT_SUCCESS && bluetoothGattCharacteristic != null) {
            _continuation?.resumeWith(Result.success(bluetoothGattCharacteristic))
        } else {
            _continuation?.resumeWith(Result.failure(CharacteristicReadException()))
        }
    }
}
