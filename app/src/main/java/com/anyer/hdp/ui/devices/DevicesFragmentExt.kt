package com.anyer.hdp.ui.devices

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8
import android.widget.Toast
import com.anyer.hdp.bluetooth.Bluetooth

val DevicesFragment.gattCallback: BluetoothGattCallback
    get() = object : BluetoothGattCallback() {
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


            activity?.runOnUiThread {
                Toast.makeText(context, "Subscribed to Heart Rate Service", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
        ) {
            if (characteristic == null) return

            val heartRateMeasurement = characteristic.getIntValue(FORMAT_UINT8, 1)

            activity?.runOnUiThread {
                Toast.makeText(context, "Heart Rate $heartRateMeasurement bpm", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }