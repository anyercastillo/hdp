package com.anyer.hdp.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8
import java.util.*

class HeartRateGattCallback(
    private val onValueChanged: (characteristic: UUID, value: Int) -> Unit
) : BluetoothGattCallback() {
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
                onValueChanged(characteristic.uuid, heartRateMeasurement)
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
    }
}
