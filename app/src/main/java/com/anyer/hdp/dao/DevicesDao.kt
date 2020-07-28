package com.anyer.hdp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anyer.hdp.db.entities.Device

@Dao
interface DevicesDao {
    @Query("SELECT * from devices ORDER BY name ASC")
    fun getDevices(): LiveData<List<Device>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevices(devices: List<Device>)

    @Query("UPDATE devices SET heartRate=:value WHERE address=:address")
    fun updateHeartRate(address: String, value: Int)

    @Query("UPDATE devices SET bodySensorLocation=:value WHERE address=:address")
    fun updateBodySensorLocation(address: String, value: Int)

    @Query("UPDATE devices SET connected=:connected WHERE address=:address")
    fun updateConnected(address: String, connected: Boolean)

    @Query("SELECT * from devices WHERE address=:address LIMIT 1")
    fun getDevice(address: String): LiveData<Device>
}