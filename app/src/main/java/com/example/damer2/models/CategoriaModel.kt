package com.example.damer2.models

import com.google.gson.annotations.SerializedName

class CategoriaModel {
    var id: String = ""
    var codigo: String = ""
    var descripcion: String = ""
    var productos : List<ProductoModel> = listOf()
    var tipoDato : String = ""
}