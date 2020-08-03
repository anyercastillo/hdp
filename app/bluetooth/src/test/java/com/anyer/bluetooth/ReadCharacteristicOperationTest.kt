package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class ReadCharacteristicOperationTest {
    private val uuid = UUID.fromString("00000000-0000-0000-0000-000000000000")

    @Test
    fun `it calls read characteristic`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val operation = ReadCharacteristicOperation(mockBluetoothGatt, uuid, uuid, {})

        operation.execute()

        verify(mockBluetoothGatt, times(1)).readCharacteristic(anyOrNull())
    }

    @Test
    fun `it calls onComplete callback`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val mockOnCompleteCallback: (result: Result<BluetoothGattCharacteristic?>) -> Unit = mock()
        val mockBluetoothGattCharacteristic: BluetoothGattCharacteristic = mock()
        val result = Result.success(mockBluetoothGattCharacteristic)

        ReadCharacteristicOperation(mockBluetoothGatt, uuid, uuid, mockOnCompleteCallback).complete(
            result
        )

        verify(mockOnCompleteCallback, times(1)).invoke(result)
    }
}