package com.anyer.hdp.ui.devices

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anyer.hdp.bluetooth.HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC
import com.anyer.hdp.bluetooth.HEART_RATE_MEASUREMENT_CHARACTERISTIC
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class DevicesViewModel(
    private val repository: AppRepository
) : ViewModel() {
    private val _scanning = MutableLiveData<Boolean>()
    val scanning: LiveData<Boolean> = _scanning

    private val _scanProgress = MutableLiveData<Int>()
    val scanProgress: LiveData<Int> = _scanProgress

    val scanProgressMax = 5 // seconds

    private val scanProgressCounter: CountDownTimer =
        object : CountDownTimer(scanProgressMax * 1000L, 100L) {
            override fun onFinish() {
                stopScanDevices()
            }

            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = millisUntilFinished / 1000L
                val progress = scanProgressMax - secondsUntilFinished
                _scanProgress.postValue(progress.toInt())
            }
        }

    val devices: LiveData<List<BleDevice>> = repository.allDevices()

    private val _connectAddresses = MediatorLiveData<List<String>>()
    val connectAddresses: LiveData<List<String>> = _connectAddresses

    init {
        _connectAddresses.addSource(devices) {
            computeConnectAddresses()
        }

        _connectAddresses.addSource(scanning) {
            computeConnectAddresses()
        }
    }

    fun onSwitchChanged(isChecked: Boolean) {
        if (isChecked) {
            startScanDevices()
        } else {
            stopScanDevices()
        }
    }

    fun onBluetoothScanChanged(devices: List<BleDevice>) {
        repository.updateDevices(devices.toSet())
    }

    fun onCharacteristicChanged(address: String, characteristic: UUID, value: Int) {
        when (characteristic) {
            HEART_RATE_MEASUREMENT_CHARACTERISTIC -> {
                updateHeartRate(address, value)
            }

            HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC -> {
                updateBodySensorLocation(address, value)
            }
        }
    }

    private fun updateHeartRate(address: String, value: Int) {
        repository.updateHeartRate(address, value)
    }

    private fun updateBodySensorLocation(address: String, value: Int) {
        repository.updateBodySensorLocation(address, value)
    }

    private fun startScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(true)
        scanProgressCounter.start()
    }

    private fun stopScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(false)
        scanProgressCounter.cancel()
    }

    private fun computeConnectAddresses() {
        if (scanning.value != true) {
            _connectAddresses.value = devices.value?.map { device -> device.address }
        }
    }
}