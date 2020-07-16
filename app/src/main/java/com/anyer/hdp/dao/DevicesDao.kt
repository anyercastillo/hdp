package com.anyer.hdp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.anyer.hdp.db.entities.Device

@Dao
interface DevicesDao {
    @Query("SELECT * from devices ORDER BY name ASC")
    fun getDevices(): LiveData<List<Device>>
}