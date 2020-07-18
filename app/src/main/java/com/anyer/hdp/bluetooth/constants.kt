package com.anyer.hdp.bluetooth

import java.util.*


val HEART_RATE_SERVICE: UUID = UUID.fromString("0000180D-0000-1000-8000-00805F9B34FB")

val HEART_RATE_MEASUREMENT_CHARACTERISTIC: UUID =
    UUID.fromString("00002A37-0000-1000-8000-00805F9B34FB")

val HEART_RATE_BODY_SENSOR_LOCATION_CHARACTERISTIC: UUID =
    UUID.fromString("00002A38-0000-1000-8000-00805F9B34FB")