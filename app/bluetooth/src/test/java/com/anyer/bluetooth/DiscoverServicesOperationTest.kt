package com.anyer.bluetooth

import android.bluetooth.BluetoothGatt
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class DiscoverServicesOperationTest {
    @Test
    fun `it calls discover services`() {
        val mockBluetoothGatt: BluetoothGatt = mock()
        val operation = DiscoverServicesOperation(mockBluetoothGatt)

        operation.execute()

        verify(mockBluetoothGatt, times(1)).discoverServices()
    }
}