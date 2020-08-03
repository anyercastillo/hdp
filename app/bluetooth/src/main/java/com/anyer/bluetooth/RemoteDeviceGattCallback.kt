package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

open class RemoteDeviceGattCallback(
    private val operationsManager: OperationsManager,
    val subscription: CharacteristicSubscription
) : BluetoothGattCallback() {
    init {
        operationsManager.add(ConnectingOperation())
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        gatt ?: return

        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
            operationsManager.complete(Result.success(null))
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        operationsManager.complete(Result.success(null))
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        operationsManager.complete(Result.success(characteristic))
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        operationsManager.complete(Result.success(null))
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        subscription.notify(Result.success(characteristic))
    }
}