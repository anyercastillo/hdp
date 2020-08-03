package com.anyer.bluetooth.hilt

import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.anyer.bluetooth.Operation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

@Module
@InstallIn(ApplicationComponent::class)
object BluetoothModule {
    @Provides
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter()
    }

    @Provides
    fun provideOperationQueue(): Queue<Operation> {
        return LinkedList()
    }
}