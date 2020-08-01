package com.anyer.bluetooth

import android.bluetooth.BluetoothGattCharacteristic

interface Operation {
    fun execute()
    fun complete(result: Result<BluetoothGattCharacteristic?>)
}
