package com.anyer.hdp.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class HeartRateGattCallback(
    private val scope: CoroutineScope,
    private val onChange: (characteristic: UUID, value: Int) -> Unit
) : BluetoothGattCallback() {
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        if (gatt == null) return

        if (newState == BluetoothGatt.STATE_CONNECTED) {
            gatt.requestMtu(256)
        }
    }

    override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        gatt?.discoverServices()
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (gatt == null) return

        subscribeToMeasureCharacteristic(gatt)
        readCharacteristic(gatt, HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC)
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        if (gatt == null) return
        if (characteristic == null) return

        when (characteristic.uuid) {
            HEART_RATE_MEASUREMENT_CHARACTERISTIC -> {
                val heartRateMeasurement = characteristic.getIntValue(FORMAT_UINT8, 1)
                onChange(characteristic.uuid, heartRateMeasurement)
            }
        }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        if (gatt == null) return
        if (characteristic == null) return

        when (characteristic.uuid) {
            HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC -> {
                val bodySensorLocation = characteristic.getIntValue(FORMAT_UINT8, 0)
                onChange(characteristic.uuid, bodySensorLocation)
            }
        }
    }

    private fun readCharacteristic(gatt: BluetoothGatt, uuid: UUID) = scope.launch {
        val characteristic = gatt.getService(HEART_RATE_SERVICE)?.getCharacteristic(uuid)

        while (scope.isActive) {
            gatt.readCharacteristic(characteristic)
            delay(1000L)
        }
    }

    private fun subscribeToMeasureCharacteristic(gatt: BluetoothGatt) {
        val characteristic = gatt.getService(HEART_RATE_SERVICE)
            ?.getCharacteristic(HEART_RATE_MEASUREMENT_CHARACTERISTIC) ?: return

        gatt.setCharacteristicNotification(characteristic, true)
    }
}
