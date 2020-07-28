package com.anyer.hdp.ui.devices

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8
import android.os.CountDownTimer
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anyer.bluetooth.*
import com.anyer.hdp.bluetooth.HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC
import com.anyer.hdp.bluetooth.HEART_RATE_MEASUREMENT_CHARACTERISTIC
import com.anyer.hdp.bluetooth.HEART_RATE_SERVICE
import com.anyer.hdp.models.BleDevice
import com.anyer.hdp.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DevicesViewModel @ViewModelInject constructor(
    private val repository: AppRepository
) : ViewModel() {
    private val subscriptions = mutableMapOf<String, CharacteristicSubscription>()

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

    override fun onCleared() {
        super.onCleared()
        subscriptions.onEach {
            it.value.unsubscribe()
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

    fun getDevice(address: String) = repository.getDevice(address)

    fun readBodySensorLocation(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val value = readCharacteristicCoroutine(
                BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address),
                ReadCharacteristicCallback(
                    HEART_RATE_SERVICE,
                    HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC
                )
            )
            repository.updateBodySensorLocation(
                address,
                value.getIntValue(FORMAT_UINT8, 0)
            )
        }
    }

    fun subscribeToHearRateMeasurement(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val subscription = subscribeToCharacteristicCoroutine(
                BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address),
                SubscribeCharacteristicCallback(
                    HEART_RATE_SERVICE,
                    HEART_RATE_MEASUREMENT_CHARACTERISTIC,
                    CharacteristicSubscription()
                )
            )

            disconnectDevice(address)
            subscriptions[address] = subscription

            subscription.onValueChange {
                repository.updateHeartRate(address, it.getIntValue(FORMAT_UINT8, 1))
            }
        }
    }

    fun disconnectDevice(address: String) {
        subscriptions[address]?.unsubscribe()
    }

    private fun startScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(true)
        scanProgressCounter.start()
    }

    private fun stopScanDevices() = CoroutineScope(Dispatchers.IO).launch {
        _scanning.postValue(false)
        scanProgressCounter.cancel()
    }
}