package com.anyer.hdp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.anyer.hdp.db.AppRoomDatabase
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository

    val allDevices: LiveData<List<BleDevice>>

    init {
        val devicesDao = AppRoomDatabase.getDatabase(application).devicesDao()
        repository = AppRepository(devicesDao)

        allDevices = liveData {
            emitSource(repository.allDevices())
        }
    }
}