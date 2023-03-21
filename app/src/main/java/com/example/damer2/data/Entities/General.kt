package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class General (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var parametro: String = "",
    var descripcion: String = "",
    var valor : String = "",
    var estado: Int = 1


)