package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt

/**
 * A data class holder for a [BluetoothGatt] and [RemoteDeviceGattCallback]
 */
data class ConnectionInfo(val bluetoothGatt: BluetoothGatt, val callback: RemoteDeviceGattCallback)