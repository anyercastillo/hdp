package com.anyer.bluetooth

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class OperationsManagerTest {
    @Test
    fun `it adds the operation to the queue`() {
        val mockOperation: Operation = mock()
        val mockQueue: Queue<Operation> = mock()
        val operationManager = OperationsManager(mockQueue)

        operationManager.add(mockOperation)

        verify(mockQueue, times(1)).add(mockOperation)
    }

    @Test
    fun `it completes operations`() {
        val mockOperation: Operation = mock()
        val operationManager = OperationsManager(LinkedList())

        operationManager.add(mockOperation)
        Assert.assertEquals(mockOperation, operationManager.currentOperation)

        operationManager.complete(Result.success(null))
        Assert.assertEquals(null, operationManager.currentOperation)
    }

    @Test
    fun `it processes the operation if the queue is empty`() {
        val mockOperation: Operation = mock()
        val operationManager = OperationsManager(LinkedList())

        operationManager.add(mockOperation)

        verify(mockOperation, times(1)).execute()
    }

    @Test
    fun `it processes a second operation after the first one is done`() {
        val mockOperation1: Operation = mock()
        val mockOperation2: Operation = mock()
        val queue: Queue<Operation> = LinkedList()
        val operationManager = OperationsManager(queue)

        operationManager.add(mockOperation1)
        operationManager.add(mockOperation2)

        verify(mockOperation2, never()).execute()

        operationManager.complete(Result.success(null))

        verify(mockOperation2, times(1)).execute()
    }
}