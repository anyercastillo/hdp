package com.anyer.hdp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anyer.hdp.db.AppRoomDatabase
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository

    init {
        val devicesDao = AppRoomDatabase.getDatabase(application).devicesDao()
        repository = AppRepository(devicesDao)
    }

    fun allDevices(context: Context): LiveData<List<BleDevice>> {
        return repository.allDevices(context)
    }
}