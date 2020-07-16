package com.anyer.hdp.ui.devices
//
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothGatt
//import android.bluetooth.BluetoothGattCallback
//import android.bluetooth.BluetoothGattCharacteristic
//import android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8
//import android.bluetooth.le.ScanResult
//import android.widget.Toast
//import androidx.navigation.fragment.findNavController
//import com.anyer.hdp.bluetooth.Bluetooth
//import com.anyer.hdp.bluetooth.BluetoothScanCallback
//import com.anyer.hdp.R
//
//val DevicesFragment.gattCallback: BluetoothGattCallback
//    get() = object : BluetoothGattCallback() {
//        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
//            if (gatt == null) return
//
//            if (newState == BluetoothGatt.STATE_CONNECTED) {
//                gatt.requestMtu(256)
//                gatt.discoverServices()
//            }
//        }
//
//        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
//            if (gatt == null) return
//            val characteristic = gatt.getService(Bluetooth.HEART_RATE_SERVICE)
//                ?.getCharacteristic(Bluetooth.HEART_RATE_MEASUREMENT_CHARACTERISTIC) ?: return
//
//            gatt.readCharacteristic(characteristic)
//            gatt.setCharacteristicNotification(characteristic, true)
//            gatt.writeCharacteristic(characteristic)
//
//            activity?.runOnUiThread {
//                Toast.makeText(context, "Subscribed to Heart Rate Service", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        override fun onCharacteristicChanged(
//            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
//        ) {
//            if (characteristic == null) return
//
//            val heartRateMeasurement = characteristic.getIntValue(FORMAT_UINT8, 1)
//
//            activity?.runOnUiThread {
//                Toast.makeText(context, "Heart Rate $heartRateMeasurement bpm", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//val DevicesFragment.bluetoothScanCallback: BluetoothScanCallback
//    get() = object : BluetoothScanCallback() {
//        val devices = mutableListOf<BluetoothDevice>()
//
//        override fun onScanResult(callbackType: Int, result: ScanResult?) {
//            if (result == null) return
//
//            devicesFound(listOf(result.device))
//        }
//
//        override fun onScanFailed(errorCode: Int) {
//            findNavController().navigate(R.id.disabledBluetoothFragment)
//        }
//
//        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
//            if (results == null) return
//
//            devicesFound(results.map { it.device })
//        }
//
//        fun devicesFound(results: List<BluetoothDevice>) {
//            devices.addAll(results)
//            devicesAdapter.submitList(devices)
//
//            stopScan()
//        }
//    }