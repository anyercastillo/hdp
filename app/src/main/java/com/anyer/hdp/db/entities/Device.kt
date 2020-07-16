package com.anyer.hdp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(@PrimaryKey val address: String, val name: String)