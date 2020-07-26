package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import kotlin.coroutines.Continuation
import org.mockito.Mockito.`when` as whenever


@RunWith(MockitoJUnitRunner::class)
class SubscribeCharacteristicCallbackUnitTest {
    @Mock
    lateinit var mockContinuation: Continuation<Unit>

    @Mock
    lateinit var mockBluetoothGatt: BluetoothGatt

    @Mock
    lateinit var mockBluetoothGattService: BluetoothGattService

    @Mock
    lateinit var mockBluetoothGattCharacteristic: BluetoothGattCharacteristic

    @Mock
    lateinit var mockDescriptor: BluetoothGattDescriptor

    @Mock
    lateinit var onValueChanged: (BluetoothGattCharacteristic) -> Unit

    @Mock
    lateinit var mockCharacteristicSubscription: CharacteristicSubscription

    @Test
    fun `it requests MTU-256 if connected`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        callback.onConnectionStateChange(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS,
            BluetoothGatt.STATE_CONNECTED
        )

        verify(
            mockBluetoothGatt,
            times(1)
        ).requestMtu(256)
    }

    @Test
    fun `it does not request MTU-256 if not connected`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        callback.onConnectionStateChange(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS,
            BluetoothGatt.STATE_DISCONNECTED
        )

        verify(
            mockBluetoothGatt,
            never()
        ).requestMtu(256)
    }

    @Test
    fun `it does not request MTU-256 if an error happen`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        callback.onConnectionStateChange(
            mockBluetoothGatt,
            BluetoothGatt.GATT_FAILURE,
            BluetoothGatt.STATE_CONNECTED
        )

        verify(
            mockBluetoothGatt,
            never()
        ).requestMtu(256)
    }

    @Test
    fun `it resumes with success if services are discovered`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        whenever(mockBluetoothGattCharacteristic.getDescriptor(ArgumentMatchers.any())).thenReturn(
            mockDescriptor
        )
        whenever(mockBluetoothGattService.getCharacteristic(uuid)).thenReturn(
            mockBluetoothGattCharacteristic
        )
        whenever(mockBluetoothGatt.getService(uuid)).thenReturn(mockBluetoothGattService)

        callback.onServicesDiscovered(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(
            mockContinuation,
            times(1)
        ).resumeWith(Result.success(Unit))
    }

    @Test
    fun `it resumes with CharacteristicNotificationException if a descriptor is not found`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        whenever(mockBluetoothGattCharacteristic.getDescriptor(ArgumentMatchers.any())).thenReturn(
            null
        )
        whenever(mockBluetoothGattService.getCharacteristic(uuid)).thenReturn(
            mockBluetoothGattCharacteristic
        )
        whenever(mockBluetoothGatt.getService(uuid)).thenReturn(mockBluetoothGattService)

        callback.onServicesDiscovered(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(
            mockContinuation,
            times(1)
        ).resumeWith(Result.failure(CharacteristicSubscribeException("Descriptor not found")))
    }

    @Test
    fun `it notifies the subscription everytime the characteristic changes`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        callback.onCharacteristicChanged(
            mockBluetoothGatt,
            mockBluetoothGattCharacteristic
        )

        verify(
            mockCharacteristicSubscription,
            times(1)
        ).notify(mockBluetoothGatt, mockBluetoothGattCharacteristic)
    }

    @Test
    fun `it does not call onValueChanged for null characteristics`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        callback.onCharacteristicChanged(
            mockBluetoothGatt,
            null
        )

        verify(
            onValueChanged,
            never()
        ).invoke(mockBluetoothGattCharacteristic)
    }

    @Test
    fun `it resumes with CharacteristicNotificationException if a failure happen`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        callback.onServicesDiscovered(
            mockBluetoothGatt,
            BluetoothGatt.GATT_FAILURE
        )

        verify(
            mockContinuation,
            times(1)
        ).resumeWith(Result.failure(CharacteristicSubscribeException("Service Discover fails")))
    }

    @Test
    fun `it resumes with CharacteristicNotificationException if the bluetooth gatt is null`() {
        val uuid = UUID.randomUUID()
        val callback = SubscribeCharacteristicCallback(uuid, uuid, mockCharacteristicSubscription)
        callback.setContinuation(mockContinuation)

        callback.onServicesDiscovered(
            null,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(
            mockContinuation,
            times(1)
        ).resumeWith(Result.failure(CharacteristicSubscribeException("Service Discover fails")))
    }
}
