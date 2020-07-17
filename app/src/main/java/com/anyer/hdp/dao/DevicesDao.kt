package com.anyer.hdp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anyer.hdp.db.entities.Device
import com.anyer.hdp.models.BleDevice

@Dao
interface DevicesDao {
    @Query("SELECT * from devices ORDER BY name ASC")
    fun getDevices(): LiveData<List<Device>>

    @Query("DELETE FROM devices")
    fun removeDevices()

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevices(devices: List<Device>)

    @Query("UPDATE devices SET heartRate=:value WHERE address=:address")
    fun updateHeartRate(address: String, value: Int)
}