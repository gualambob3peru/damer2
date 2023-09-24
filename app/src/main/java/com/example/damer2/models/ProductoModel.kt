package com.example.damer2.models

import com.google.gson.annotations.SerializedName

class ProductoModel {
    var id: String = ""
    var sku: String = ""
    var cod_categoria: String = ""
    var descripcion : String =""
    var vant : String = "0"
    var cod_distrito : String =""
    var cod_canal : String =""
    var cod_zona : String = ""
    var compra_ant : String ="0"
    var compra : String =""
    var inventario : String =""
    var precio : String =""
    var estado : String="1"
    var fabricante : String = ""
    var marca : String = ""
    var peso : String = ""
    var imagen : String = ""
    var estadoEnviado : Int = 0
    var estadoCambiado: Int = 0
    var cambioNegocio: String=""
    var estadoExcluido : Int = 0 //0-> No tiene categorias exluidas pendientes, 1 -> Si
    var estadoGuardado : Int = 0
    var estadoTemporal : Int = 1
    var precio_ant : String = "0"
    var ve : String = "0"
}