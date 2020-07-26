package com.anyer.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import kotlin.coroutines.Continuation


@RunWith(MockitoJUnitRunner::class)
class ReadCharacteristicCoroutineUnitTest {
    @Mock
    lateinit var mockBluetoothGatt: BluetoothGatt

    @Mock
    lateinit var mockBluetoothDevice: BluetoothDevice

    @Mock
    lateinit var mockBluetoothGattCharacteristic: BluetoothGattCharacteristic

    class MockReadCharacteristicCallbackSuccess(
        private val mockBluetoothGattCharacteristic: BluetoothGattCharacteristic,
        service: UUID,
        characteristic: UUID
    ) :
        ReadCharacteristicCallback(service, characteristic) {

        override fun setContinuation(continuation: Continuation<BluetoothGattCharacteristic>) {
            continuation.resumeWith(Result.success(mockBluetoothGattCharacteristic))
        }
    }

    class MockReadCharacteristicCallbackFailure(
        service: UUID,
        characteristic: UUID
    ) :
        ReadCharacteristicCallback(service, characteristic) {

        override fun setContinuation(continuation: Continuation<BluetoothGattCharacteristic>) {
            continuation.resumeWith(Result.failure(CharacteristicReadException()))
        }
    }

    @Test
    fun `it disconnects after a successful call`() = runBlocking {
        val uuid = UUID.randomUUID()
        val callback =
            MockReadCharacteristicCallbackSuccess(mockBluetoothGattCharacteristic, uuid, uuid)

        doReturn(mockBluetoothGatt).`when`(mockBluetoothDevice).connectGatt(null, false, callback)

        readCharacteristicCoroutine(
            mockBluetoothDevice,
            callback
        )

        verify(
            mockBluetoothGatt,
            times(1)
        ).disconnect()
    }

    @Test(expected = CharacteristicReadException::class)
    fun `it throws an exception after a failure call`() = runBlocking {
        val uuid = UUID.randomUUID()

        val callback = MockReadCharacteristicCallbackFailure(uuid, uuid)

        doReturn(mockBluetoothGatt).`when`(mockBluetoothDevice).connectGatt(null, false, callback)

        readCharacteristicCoroutine(
            mockBluetoothDevice,
            callback
        )

        assert(true)
    }

    @Test
    fun `it resolves the bluetooth characteristic`() = runBlocking {
        val uuid = UUID.randomUUID()
        val callback =
            MockReadCharacteristicCallbackSuccess(mockBluetoothGattCharacteristic, uuid, uuid)

        doReturn(mockBluetoothGatt).`when`(mockBluetoothDevice).connectGatt(null, false, callback)

        val bluetoothGattCharacteristic = readCharacteristicCoroutine(
            mockBluetoothDevice,
            callback
        )

        Assert.assertEquals(mockBluetoothGattCharacteristic, bluetoothGattCharacteristic)
    }
}
