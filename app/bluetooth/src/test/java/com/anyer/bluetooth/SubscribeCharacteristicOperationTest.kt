package com.anyer.bluetooth

import android.bluetooth.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class SubscribeCharacteristicOperationTest {
    private val uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")

    @Test
    fun `it calls setup a subscription to a gatt service`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockService: BluetoothGattService = mock()
        val mockCharacteristic: BluetoothGattCharacteristic = mock()
        val mockDescriptor: BluetoothGattDescriptor = mock()

        whenever(
            mockCharacteristic.getDescriptor(
                SubscribeCharacteristicOperation.DESCRIPTOR_NOTIFICATION_UUID
            )
        ).thenReturn(mockDescriptor)
        whenever(mockService.getCharacteristic(uuid)).thenReturn(mockCharacteristic)
        whenever(mockBluetoothGatt.getService(uuid)).thenReturn(mockService)

        SubscribeCharacteristicOperation(mockBluetoothGatt, uuid, uuid, {}).execute()

        verify(mockBluetoothGatt, times(1)).setCharacteristicNotification(mockCharacteristic, true)
        verify(mockDescriptor, times(1)).setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        verify(mockBluetoothGatt, times(1)).writeDescriptor(mockDescriptor)
    }

    @Test
    fun `it calls onComplete callback`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockOnCompleteCallback: (result: Result<BluetoothGattCharacteristic?>) -> Unit = mock()
        val mockBluetoothGattCharacteristic: BluetoothGattCharacteristic = mock()
        val result = Result.success(mockBluetoothGattCharacteristic)

        SubscribeCharacteristicOperation(
            mockBluetoothGatt,
            uuid,
            uuid,
            mockOnCompleteCallback
        ).complete(
            result
        )

        verify(mockOnCompleteCallback, times(1)).invoke(result)
    }
}