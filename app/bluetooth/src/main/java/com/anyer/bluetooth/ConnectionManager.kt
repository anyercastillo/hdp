package com.anyer.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattCharacteristic
import androidx.lifecycle.LifecycleObserver
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class ConnectionManager(
    private val adapter: BluetoothAdapter,
    private val operationsManager: OperationsManager
) : LifecycleObserver {
    private val connections = ConcurrentHashMap<String, ConnectionInfo>()

    fun disconnectFrom(address: String) {
        val bluetoothGatt = connections[address]?.bluetoothGatt

        bluetoothGatt?.disconnect()

        // Ideally `gatt.close()` should be called at `BluetoothGattCallback.onConnectionStateChange`
        // when `state = STATE_DISCONNECTED`. But there are not warranties for this to happen at the
        // Android Bluetooth API level.
        // The safest workaround is just to call `close()` after some time.
        // TODO: Use 1.5x of the round-trip defined in the peripheral specs.
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            bluetoothGatt?.close()
        }
    }

    suspend fun readCharacteristic(
        address: String,
        service: UUID,
        characteristic: UUID
    ): BluetoothGattCharacteristic = withContext(Dispatchers.IO) {
        if (isNotConnected(address)) {
            connectTo(address)
        }

        val gatt = connections[address]?.bluetoothGatt ?: throw Exception()

        suspendCoroutine { continuation: Continuation<BluetoothGattCharacteristic> ->
            operationsManager.add(
                ReadCharacteristicOperation(
                    gatt,
                    service,
                    characteristic,
                    onComplete = { result ->
                        // TODO result.fail
                        val value = result.getOrNull() ?: throw Exception()
                        continuation.resumeWith(Result.success(value))
                    }
                )
            )
        }
    }

    suspend fun subscribeCharacteristic(
        address: String,
        service: UUID,
        characteristic: UUID
    ): CharacteristicSubscription = withContext(Dispatchers.IO) {
        if (isNotConnected(address)) {
            connectTo(address)
        }

        val gatt = connections[address]?.bluetoothGatt ?: throw Exception()
        val subscription = connections[address]?.callback?.subscription ?: throw Exception()

        suspendCoroutine { continuation: Continuation<BluetoothGattCharacteristic?> ->
            operationsManager.add(
                SubscribeCharacteristicOperation(
                    gatt,
                    service,
                    characteristic,
                    onComplete = { result ->
                        continuation.resumeWith(result)
                    }
                )
            )
        }

        return@withContext subscription
    }

    @Synchronized
    private fun connectTo(address: String) {
        if (isConnected(address)) return

        val gattCallback = RemoteDeviceGattCallback(operationsManager, CharacteristicSubscription())
        val remoteDevice = adapter.getRemoteDevice(address)
        val bluetoothGatt = remoteDevice.connectGatt(null, false, gattCallback)

        connections[address] = ConnectionInfo(bluetoothGatt, gattCallback)

        operationsManager.add(DiscoverServicesOperation(bluetoothGatt))
    }

    private fun isConnected(address: String) = connections[address] != null

    private fun isNotConnected(address: String) = !isConnected(address)
}