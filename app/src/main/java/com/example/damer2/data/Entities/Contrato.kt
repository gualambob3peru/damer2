package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contrato (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cod_categoria: String = "",
    var cod_zona: String = "",
    var cod_canal: String = "",
    var variables : String = ""
)