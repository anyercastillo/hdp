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
class SubscribeCharacteristicCoroutineUnitTest {
    @Mock
    lateinit var mockBluetoothGatt: BluetoothGatt

    @Mock
    lateinit var mockBluetoothDevice: BluetoothDevice

    @Mock
    lateinit var mockBluetoothGattCharacteristic: BluetoothGattCharacteristic

    @Mock
    lateinit var mockCharacteristicSubscription: CharacteristicSubscription

    class MockSubscribeCharacteristicCallbackSuccess(
        service: UUID,
        characteristic: UUID,
        subscription: CharacteristicSubscription
    ) :
        SubscribeCharacteristicCallback(service, characteristic, subscription) {

        override fun setContinuation(continuation: Continuation<Unit>) {
            continuation.resumeWith(Result.success(Unit))
        }
    }

    class MockSubscribeCharacteristicCallbackFailure(
        service: UUID,
        characteristic: UUID,
        subscription: CharacteristicSubscription
    ) :
        SubscribeCharacteristicCallback(service, characteristic, subscription) {

        override fun setContinuation(continuation: Continuation<Unit>) {
            continuation.resumeWith(Result.failure(CharacteristicSubscribeException("error")))
        }
    }


    @Test(expected = CharacteristicSubscribeException::class)
    fun `it throws an exception after a failure in the subscription process`() = runBlocking {
        val uuid = UUID.randomUUID()

        val callback =
            MockSubscribeCharacteristicCallbackFailure(uuid, uuid, mockCharacteristicSubscription)

        doReturn(mockBluetoothGatt).`when`(mockBluetoothDevice).connectGatt(null, false, callback)

        subscribeToCharacteristicCoroutine(
            mockBluetoothDevice,
            callback
        )

        assert(true)
    }

    @Test
    fun `it returns a CharacteristicSubscription`() = runBlocking {
        val uuid = UUID.randomUUID()
        val callback =
            MockSubscribeCharacteristicCallbackSuccess(uuid, uuid, mockCharacteristicSubscription)

        doReturn(mockBluetoothGatt).`when`(mockBluetoothDevice).connectGatt(null, false, callback)

        val subscription = subscribeToCharacteristicCoroutine(
            mockBluetoothDevice,
            callback
        )

        Assert.assertEquals(subscription, mockCharacteristicSubscription)
    }

    @Test
    fun `it notifies on new characteristic values`() = runBlocking {
        val uuid = UUID.randomUUID()
        val callback =
            MockSubscribeCharacteristicCallbackSuccess(uuid, uuid, mockCharacteristicSubscription)

        doReturn(mockBluetoothGatt).`when`(mockBluetoothDevice).connectGatt(null, false, callback)

        subscribeToCharacteristicCoroutine(
            mockBluetoothDevice,
            callback
        )

        callback.onCharacteristicChanged(mockBluetoothGatt, mockBluetoothGattCharacteristic)

        verify(mockCharacteristicSubscription, times(1))
            .notify(
                mockBluetoothGatt,
                mockBluetoothGattCharacteristic
            )
    }
}
