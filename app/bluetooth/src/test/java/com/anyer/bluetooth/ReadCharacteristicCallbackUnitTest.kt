package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import kotlin.coroutines.Continuation
import org.mockito.Mockito.`when` as whenever


@RunWith(MockitoJUnitRunner::class)
class ReadCharacteristicCallbackUnitTest {
    @Mock
    lateinit var mockContinuation: Continuation<BluetoothGattCharacteristic>

    @Mock
    lateinit var mockBluetoothGatt: BluetoothGatt

    @Mock
    lateinit var mockBluetoothGattService: BluetoothGattService

    @Mock
    lateinit var mockBluetoothGattCharacteristic: BluetoothGattCharacteristic

    @Test
    fun `it discovers services if connected`() {
        val uuid = UUID.randomUUID()
        val callback = ReadCharacteristicCallback(uuid, uuid)

        callback.onConnectionStateChange(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS,
            BluetoothGatt.STATE_CONNECTED
        )

        verify(
            mockBluetoothGatt,
            times(1)
        ).discoverServices()
    }

    @Test
    fun `it reads characteristic if services are discovered`() {
        val uuid = UUID.randomUUID()
        val callback = ReadCharacteristicCallback(uuid, uuid)

        whenever(mockBluetoothGattService.getCharacteristic(uuid)).thenReturn(
            mockBluetoothGattCharacteristic
        )
        whenever(mockBluetoothGatt.getService(uuid)).thenReturn(mockBluetoothGattService)

        callback.onServicesDiscovered(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(
            mockBluetoothGatt,
            times(1)
        ).readCharacteristic(mockBluetoothGattCharacteristic)
    }

    @Test
    fun `it does not read characteristic if not connected`() {
        val uuid = UUID.randomUUID()
        val callback = ReadCharacteristicCallback(uuid, uuid)

        callback.onConnectionStateChange(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS,
            BluetoothGatt.STATE_DISCONNECTED
        )

        verify(
            mockBluetoothGatt,
            never()
        ).readCharacteristic(mockBluetoothGattCharacteristic)
    }

    @Test
    fun `it does not discover services if not connected`() {
        val uuid = UUID.randomUUID()
        val callback = ReadCharacteristicCallback(uuid, uuid)

        callback.onConnectionStateChange(
            mockBluetoothGatt,
            BluetoothGatt.GATT_FAILURE,
            BluetoothGatt.STATE_CONNECTED
        )

        verify(
            mockBluetoothGatt,
            never()
        ).discoverServices()
    }

    @Test
    fun `it resumes with success after read a characteristic with no errors`() {
        val uuid = UUID.randomUUID()
        val callback = ReadCharacteristicCallback(uuid, uuid)
        callback.setContinuation(mockContinuation)

        callback.onCharacteristicRead(
            mockBluetoothGatt,
            mockBluetoothGattCharacteristic,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(
            mockContinuation,
            times(1)
        ).resumeWith(Result.success(mockBluetoothGattCharacteristic))
    }

    @Test
    fun `it resumes with CharacteristicReadException if a failure happen`() {
        val uuid = UUID.randomUUID()
        val callback = ReadCharacteristicCallback(uuid, uuid)
        callback.setContinuation(mockContinuation)

        callback.onCharacteristicRead(
            mockBluetoothGatt,
            mockBluetoothGattCharacteristic,
            BluetoothGatt.GATT_FAILURE
        )

        verify(
            mockContinuation,
            times(1)
        ).resumeWith(Result.failure(CharacteristicReadException()))
    }

    @Test
    fun `it resumes with CharacteristicReadException if a characteristic is null`() {
        val uuid = UUID.randomUUID()
        val callback = ReadCharacteristicCallback(uuid, uuid)
        callback.setContinuation(mockContinuation)

        callback.onCharacteristicRead(
            mockBluetoothGatt,
            null,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(
            mockContinuation,
            times(1)
        ).resumeWith(Result.failure(CharacteristicReadException()))
    }
}
