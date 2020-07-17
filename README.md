# POC 
The main goal of this project is to interface with **Bluetooth Heart Rate Service.** 


# Technologies:
- Bluetooth Gatt https://developer.android.com/reference/android/bluetooth/BluetoothGatt 
- Navigation https://developer.android.com/guide/navigation/
- RecyclerView with ListAdapter for a better performance
- Material Design
- Room
- ViewModel
- LiveData
- Coroutines

# Things learnt
- BluetoothHealth was deprecated in API level 29. 
New apps should use Bluetooth Low Energy based solutions such as BluetoothGatt.

# BLE Peripheral Simulator
https://play.google.com/store/apps/details?id=io.github.webbluetoothcg.bletestperipheral&hl=en_US

# BLE specs
https://www.bluetooth.com/xml-viewer/?src=https://www.bluetooth.com/wp-content/uploads/Sitecore-Media-Library/Gatt/Xml/Characteristics/org.bluetooth.characteristic.heart_rate_measurement.xml#tree0:0,6,1|0,7;

## How to test it.
1. Enable Bluetooth.
2. Launch the app.
3. Scan for new devices broadcasting heart rate.
4. Each device listed will auto-refresh if the data change.
5. Trigger a "heart rate" from the BLE device or change the "body sensor location" value.

