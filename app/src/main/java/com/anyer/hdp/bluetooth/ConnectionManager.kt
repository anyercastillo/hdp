package com.anyer.hdp.bluetooth

import android.bluetooth.BluetoothGatt
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ConnectionManager(
    private val context: Context,
    private val factory: HeartRateGattCallbackFactory
) : LifecycleObserver {

    // Map of <address, connection>
    private val connectionsMap = mutableMapOf<String, BluetoothGatt>()

    fun updateConnections(addresses: List<String>) {
        val inactiveAddresses = connectionsMap.keys.subtract(addresses)
        disconnectInactiveAddresses(inactiveAddresses)

        val newAddresses = addresses.subtract(connectionsMap.keys)
        connectNewAddresses(newAddresses)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disconnectAddresses() {
        disconnectInactiveAddresses(connectionsMap.keys)
    }

    private fun disconnectInactiveAddresses(inactiveAddresses: Set<String>) {
        inactiveAddresses.forEach { address ->
            connectionsMap[address]?.disconnect()
            connectionsMap.remove(address)
        }
    }

    private fun connectNewAddresses(
        newAddresses: Set<String>
    ) {
        newAddresses.forEach { address ->
            connectionsMap[address] =
                bluetoothConnectGatt(address, context, factory.create(address))
        }
    }
}