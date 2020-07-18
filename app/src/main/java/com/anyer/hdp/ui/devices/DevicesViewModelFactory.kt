package com.anyer.hdp.ui.devices

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anyer.hdp.db.AppRoomDatabase
import com.anyer.hdp.repository.AppRepository

class DevicesViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val devicesDao = AppRoomDatabase.getDatabase(context).devicesDao()
        val repository = AppRepository(devicesDao)

        @Suppress("UNCHECKED_CAST")
        return DevicesViewModel(
            repository
        ) as T
    }
}