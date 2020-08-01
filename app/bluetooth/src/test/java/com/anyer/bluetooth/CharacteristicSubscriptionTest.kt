package com.anyer.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CharacteristicSubscriptionTest {
    @Test
    fun `notify calls onValueChange`() {
        val mockCallback: (result: Result<BluetoothGattCharacteristic?>) -> Unit = mock()
        val mockBluetoothGattCharacteristic: BluetoothGattCharacteristic = mock()
        val result = Result.success(mockBluetoothGattCharacteristic)

        val characteristicSubscription = CharacteristicSubscription()
        characteristicSubscription.onValueChange(mockCallback)
        characteristicSubscription.notify(result)

        verify(mockCallback, times(1)).invoke(result)
    }
}