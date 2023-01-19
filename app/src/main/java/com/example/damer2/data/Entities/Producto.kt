package com.example.damer2.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Producto (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var sku: String = "",
    var cod_categoria: String = "",
    var cod_negocio : String ="",
    var descripcion: String = "",
    var compra : String = "0",
    var inventario : String  = "0",
    var precio : String = "0",
    var ve : String = "0",
    var estado: String = "",
    var desc_categoria : String = "",
    var vant : String ="0",
    var cod_distrito : String = "0",
    var cod_zona : String = "0",
    var cod_canal : String = "0",
    var estadoNuevo : String = "0",
    var compra_ant : String = "0",
    var fabricante : String = "",
    var marca : String = "",
    var peso : String = "",
    var imagen : String = "",

)