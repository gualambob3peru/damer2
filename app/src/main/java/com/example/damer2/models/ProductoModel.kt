package com.example.damer2.models

import com.google.gson.annotations.SerializedName

class ProductoModel {
    var id: String = ""
    var sku: String = ""
    var cod_categoria: String = ""
    var descripcion : String =""
    var vant : String = ""
    var cod_distrito : String =""
    var cod_canal : String =""
    var cod_zona : String = ""
    var compra_ant : String =""
    var fabricante : String = ""
    var marca : String = ""
    var peso : String = ""
    var imagen : String = ""
    var estadoEnviado : Int = 0
    var estadoCambiado: Int = 0
    var cambioNegocio: String=""
}