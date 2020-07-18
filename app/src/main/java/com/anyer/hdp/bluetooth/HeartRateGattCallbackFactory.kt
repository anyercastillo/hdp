package com.anyer.hdp.bluetooth

interface HeartRateGattCallbackFactory {
    fun create(address: String): HeartRateGattCallback
}
