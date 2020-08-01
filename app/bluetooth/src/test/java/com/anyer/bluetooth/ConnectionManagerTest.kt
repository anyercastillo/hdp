package com.anyer.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class ConnectionManagerTest {
    private val uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")

    @Test
    fun `readCharacteristic creates a connection if does not exist`() = runBlocking {
        val mockAdapter: BluetoothAdapter = mock()
        val mockDevice: BluetoothDevice = mock()
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockCharacteristic: BluetoothGattCharacteristic = mock()

        whenever(mockDevice.connectGatt(anyOrNull(), any(), any())).thenReturn(mockBluetoothGatt)
        whenever(mockAdapter.getRemoteDevice(any<String>())).thenReturn(mockDevice)

        val mockOperationManager = object : OperationsManager(LinkedList()) {
            override fun add(operation: Operation) {
                operation.complete(Result.success(mockCharacteristic))
            }
        }
        val connectionManager = ConnectionManager(mockAdapter, mockOperationManager)
        connectionManager.readCharacteristic("address", uuid, uuid)

        verify(mockDevice, times(1)).connectGatt(anyOrNull(), any(), any())
        assert(true)
    }

    @Test
    fun `readCharacteristic re-uses an existing connection`() = runBlocking {
        val mockAdapter: BluetoothAdapter = mock()
        val mockDevice: BluetoothDevice = mock()
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockCharacteristic: BluetoothGattCharacteristic = mock()

        whenever(mockDevice.connectGatt(anyOrNull(), any(), any())).thenReturn(mockBluetoothGatt)
        whenever(mockAdapter.getRemoteDevice(any<String>())).thenReturn(mockDevice)

        val mockOperationManager = object : OperationsManager(LinkedList()) {
            override fun add(operation: Operation) {
                operation.complete(Result.success(mockCharacteristic))
            }
        }
        val connectionManager = ConnectionManager(mockAdapter, mockOperationManager)
        connectionManager.readCharacteristic("address", uuid, uuid) // Creates the connection
        connectionManager.readCharacteristic("address", uuid, uuid) // Re-uses the connection

        verify(mockDevice, times(1)).connectGatt(anyOrNull(), any(), any())
        assert(true)
    }

    @Test
    fun `readCharacteristic return a characteristic`() = runBlocking {
        val mockAdapter: BluetoothAdapter = mock()
        val mockDevice: BluetoothDevice = mock()
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockCharacteristic: BluetoothGattCharacteristic = mock()

        whenever(mockDevice.connectGatt(anyOrNull(), any(), any())).thenReturn(mockBluetoothGatt)
        whenever(mockAdapter.getRemoteDevice(any<String>())).thenReturn(mockDevice)

        val mockOperationManager = object : OperationsManager(LinkedList()) {
            override fun add(operation: Operation) {
                operation.complete(Result.success(mockCharacteristic))
            }
        }
        val connectionManager = ConnectionManager(mockAdapter, mockOperationManager)
        val characteristic = connectionManager.readCharacteristic("address", uuid, uuid)

        Assert.assertEquals(characteristic, mockCharacteristic)
    }
}