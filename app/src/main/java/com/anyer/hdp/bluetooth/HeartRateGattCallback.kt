package com.anyer.hdp.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8
import com.anyer.hdp.bluetooth.Bluetooth

class HeartRateGattCallback(
    private val onHeartRateMeasurement: (value: Int) -> Unit
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

        val characteristic = gatt.getService(Bluetooth.HEART_RATE_SERVICE)
            ?.getCharacteristic(Bluetooth.HEART_RATE_MEASUREMENT_CHARACTERISTIC) ?: return

        gatt.readCharacteristic(characteristic)
        gatt.setCharacteristicNotification(characteristic, true)
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
    ) {
        if (gatt == null) return
        if (characteristic == null) return

        when (characteristic.uuid) {
            Bluetooth.HEART_RATE_MEASUREMENT_CHARACTERISTIC -> {
                val heartRateMeasurement = characteristic.getIntValue(FORMAT_UINT8, 1)
                onHeartRateMeasurement(heartRateMeasurement)
            }
        }
    }
}
