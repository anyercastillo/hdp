package com.anyer.hdp.ui.devices

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anyer.hdp.bluetooth.Bluetooth
import com.anyer.hdp.db.AppRoomDatabase
import com.anyer.hdp.repository.AppRepository

@Suppress("UNCHECKED_CAST")
class DevicesViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val devicesDao = AppRoomDatabase.getDatabase(context).devicesDao()
        val repository = AppRepository(devicesDao)
        val bluetooth = Bluetooth(context)

        return DevicesViewModel(
            repository,
            bluetooth
        ) as T
    }
}