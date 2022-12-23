package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Canal (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var codigo: String = "",
    var descripcion: String = "",
    var estado: String = ""
)