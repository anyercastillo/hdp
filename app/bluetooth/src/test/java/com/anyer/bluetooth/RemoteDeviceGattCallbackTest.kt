package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class RemoteDeviceGattCallbackTest {
    open class MockOperationsManager : OperationsManager(LinkedList())

    @Test
    fun `it adds a ConnectingOperation when created`() {
        val mockOperationManager: OperationsManager = mock()
        val mockSubscription: CharacteristicSubscription = mock()
        val captor = argumentCaptor<Operation>()

        RemoteDeviceGattCallback(mockOperationManager, mockSubscription)

        verify(mockOperationManager, times(1)).add(captor.capture())
        Assert.assertTrue(captor.firstValue is ConnectingOperation)
    }

    @Test
    fun `it calls complete on OperationManger when onConnectionStateChange changes to connected`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockOperationManager: MockOperationsManager = spy()
        val mockSubscription: CharacteristicSubscription = mock()

        val callback = RemoteDeviceGattCallback(mockOperationManager, mockSubscription)
        callback.onConnectionStateChange(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS,
            BluetoothGatt.STATE_CONNECTED
        )

        verify(mockOperationManager, times(1)).complete(Result.success(null))
    }

    @Test
    fun `it calls complete on OperationManger when onServicesDiscovered`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockOperationManager: MockOperationsManager = spy()
        val mockSubscription: CharacteristicSubscription = mock()

        val callback = RemoteDeviceGattCallback(mockOperationManager, mockSubscription)
        callback.onServicesDiscovered(
            mockBluetoothGatt,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(mockOperationManager, times(1)).complete(Result.success(null))
    }

    @Test
    fun `it calls complete on OperationManger when onCharacteristicRead`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockCharacteristic: BluetoothGattCharacteristic = mock()
        val mockOperationManager: MockOperationsManager = spy()
        val mockSubscription: CharacteristicSubscription = mock()

        val callback = RemoteDeviceGattCallback(mockOperationManager, mockSubscription)
        callback.onCharacteristicRead(
            mockBluetoothGatt,
            mockCharacteristic,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(mockOperationManager, times(1)).complete(Result.success(mockCharacteristic))
    }

    @Test
    fun `it calls complete on OperationManger when onDescriptorWrite`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockDescriptor: BluetoothGattDescriptor = mock()
        val mockOperationManager: MockOperationsManager = spy()
        val mockSubscription: CharacteristicSubscription = mock()

        val callback = RemoteDeviceGattCallback(mockOperationManager, mockSubscription)
        callback.onDescriptorWrite(
            mockBluetoothGatt,
            mockDescriptor,
            BluetoothGatt.GATT_SUCCESS
        )

        verify(mockOperationManager, times(1)).complete(Result.success(null))
    }

    @Test
    fun `it calls notify on CharacteristicSubscription when onCharacteristicChanged`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockCharacteristic: BluetoothGattCharacteristic = mock()
        val mockOperationManager: MockOperationsManager = spy()
        val mockSubscription: CharacteristicSubscription = mock()

        val callback = RemoteDeviceGattCallback(mockOperationManager, mockSubscription)
        callback.onCharacteristicChanged(
            mockBluetoothGatt,
            mockCharacteristic
        )

        verify(mockSubscription, times(1)).notify(Result.success(mockCharacteristic))
    }
}