package com.example.damer2.models

import com.google.gson.annotations.SerializedName

class NegocioModel {
    var id: String = ""
    var cod_negocio: String = ""
    var direccion: String = ""
    var nombre: String = ""
    var categorias : List<CategoriaModel> = listOf()
    var cod_distrito : String = "0"
    var cod_canal : String = "0"
    var cod_zona : String = "0"
    var di_descripcion: String = ""
    var vendedor: String = ""
    var telefono: String = ""
    var estadoEnviado : Int = 0
    var estado : String="1"
    var estadoTemporal : Int = 1
    var lat : String = ""
    var lgn : String = ""
}