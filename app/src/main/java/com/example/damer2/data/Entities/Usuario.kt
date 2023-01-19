package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nombres: String = "",
    var apellidoPaterno: String= "",
    var apellidoMaterno: String= "",
    var password: String= "",
    var email: String= "",
    var telefono: String= "",
    var created_at: String= "",
    var idCargo: String= "",
    var estado: String= "",
    var idTipoDocumento: String= "",
    var nroDocumento: String= "",
)