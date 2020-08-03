package com.anyer.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import com.android.example.github.testing.OpenForTesting
import java.util.*
import javax.inject.Inject

/**
 * This class guarantees one [Operation] at a time.
 */
@OpenForTesting
open class OperationsManager @Inject constructor(
    private val queue: Queue<Operation>
) {
    var currentOperation: Operation? = null
        private set

    @OpenForTesting
    @Synchronized
    open fun add(operation: Operation) {
        queue.add(operation)
        processNext()
    }

    @OpenForTesting
    @Synchronized
    // TODO: Improve function signature to accept:
    //  - event: ServicesDiscovered | DescriptorWrite | CharacteristicRead | ...
    //  - result: Result<EventValue>.
    //  Example: complete(CharacteristicRead, Result.success(BluetoothGattCharacteristic()))
    open fun complete(result: Result<BluetoothGattCharacteristic?>) {
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