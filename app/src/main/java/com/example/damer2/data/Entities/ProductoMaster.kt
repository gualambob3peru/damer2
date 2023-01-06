package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductoMaster (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var sku: String = "",
    var descripcion: String = "",
    var estado: String = "1",
    var cod_categoria: String = "",
    var cod_distrito : String ="",
    var cod_canal : String ="",
    var cod_zona : String = "",
)