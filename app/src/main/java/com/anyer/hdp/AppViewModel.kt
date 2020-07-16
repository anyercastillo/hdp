package com.anyer.hdp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.anyer.hdp.bluetooth.Bluetooth
import com.anyer.hdp.db.AppRoomDatabase
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository
    private var bluetooth: Bluetooth? = null

    init {
        val devicesDao = AppRoomDatabase.getDatabase(application).devicesDao()
        repository = AppRepository(devicesDao)
    }

    fun allDevices(): LiveData<List<BleDevice>> {
        return repository.allDevices()
    }

    fun startScanDevices(context: Context) = CoroutineScope(Dispatchers.IO).launch {
        if (bluetooth == null) {
            bluetooth = Bluetooth(context)
        }

        bluetooth?.let {
            val flow = it.startScanDevices()
            flow.collect { devices ->
                repository.updateDevices(devices)
            }
        }
    }

    fun stopScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        bluetooth?.stopScanDevices()
    }
}