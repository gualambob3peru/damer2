package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductoMaster (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var sku: String = "",
    var descripcion: String = "",
    var estado: String = "1"
)