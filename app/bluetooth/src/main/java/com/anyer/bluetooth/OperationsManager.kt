package com.anyer.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import java.util.*

open class OperationsManager(
    private val queue: Queue<Operation>
) {
    var currentOperation: Operation? = null
        private set

    @Synchronized
    fun add(operation: Operation) {
        queue.add(operation)
        processNext()
    }

    @Synchronized
    // TODO: Improve function signature to accept:
    //  - event: ServicesDiscovered | DescriptorWrite | CharacteristicRead | ...
    //  - result: Result<EventValue>.
    //  Example: complete(CharacteristicRead, Result.success(BluetoothGattCharacteristic()))
    fun complete(result: Result<BluetoothGattCharacteristic?>) {
        currentOperation?.complete(result)

        currentOperation = null
        processNext()
    }

    private fun processNext() {
        if (currentOperation != null) return

        currentOperation = queue.poll()
        currentOperation?.execute()
    }
}