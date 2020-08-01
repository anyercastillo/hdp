package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback

data class ConnectionInfo(val bluetoothGatt: BluetoothGatt, val callback: RemoteDeviceGattCallback)