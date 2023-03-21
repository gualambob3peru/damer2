package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Negocio (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var codigo_negocio: String = "",
    var descripcion: String = "",
    var vendedor : String = "",
    var telefono : String = "",
    var zona : String = "",
    var distrito : String ="",
    var canal : String ="",
    var nombre : String ="",
    var estado: String = "1",
    var estadoVi : String = "1",
    var stateDownload: String = "0",
    var di_descripcion: String = "",
    var estadoEnviado : Int = 0,
    var estadoExcluido : Int = 0,//0-> No tiene categorias exluidas pendientes, 2 -> excluido, 3 ->excluido en otro negocio
    var estadoNuevo : Int = 0,
    var estadoTemporal : Int = 1

)