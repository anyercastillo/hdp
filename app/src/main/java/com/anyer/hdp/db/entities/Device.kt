package com.anyer.hdp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    val address: String,
    @PrimaryKey val name: String,
    val heartRate: Int,
    val bodySensorLocation: Int,
    val connected: Boolean
)