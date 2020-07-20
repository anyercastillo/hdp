package com.anyer.hdp.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8
import android.bluetooth.BluetoothGattDescriptor
import java.util.*


class HeartRateGattCallback(
    private val onValueChanged: (characteristic: UUID, value: Int) -> Unit
) : BluetoothGattCallback() {
    // TODO: Needs research.
    // Seems like a call to BluetoothDevice.connectGatt(context, false, heartRateGattCallback)
    // cannot share the same callback "heartRateGattCallback" for more than one characteristic
    // BluetoothGatt.writeDescriptor(descriptor) will affect all operations in BluetoothGatt

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            gatt?.requestMtu(256)
        }
    }

    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        gatt?.discoverServices()
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        subscribeToMeasureCharacteristic(gatt)
        readCharacteristic(gatt, HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC)
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        when (characteristic?.uuid) {
            HEART_RATE_MEASUREMENT_CHARACTERISTIC -> {
                val heartRateMeasurement = characteristic.getIntValue(FORMAT_UINT8, 1)
                if (heartRateMeasurement != null) {
                    onValueChanged(characteristic.uuid, heartRateMeasurement)
                }
            }
        }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        when (characteristic?.uuid) {
            HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC -> {
                val bodySensorLocation = characteristic.getIntValue(FORMAT_UINT8, 0)
                onValueChanged(characteristic.uuid, bodySensorLocation)
            }
        }
    }

    private fun readCharacteristic(gatt: BluetoothGatt?, uuid: UUID) {
        val characteristic = gatt?.getService(HEART_RATE_SERVICE)?.getCharacteristic(uuid)
        gatt?.readCharacteristic(characteristic)
    }

    private fun subscribeToMeasureCharacteristic(gatt: BluetoothGatt?) {
        val service = gatt?.getService(HEART_RATE_SERVICE)
        val characteristic = service
            ?.getCharacteristic(HEART_RATE_MEASUREMENT_CHARACTERISTIC)

        gatt?.setCharacteristicNotification(characteristic, true)

        // A better API would be for setCharacteristicNotification to set the descriptor,
        // but unfortunately it doesn't seem to work that way.
        val uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        val descriptor = characteristic?.getDescriptor(uuid)
        descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt?.writeDescriptor(descriptor)

    }
}
