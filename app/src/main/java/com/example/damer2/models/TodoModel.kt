package com.example.damer2.models

import com.google.gson.annotations.SerializedName

class TodoModel {
    var id: String = ""
    var productos: List<ProductoModel> = listOf()
    var canals : List<CanalModel> = listOf()
    var distritos: List<DistritoModel> = listOf()
    var zonas: List<ZonaModel> = listOf()
    var categorias: List<CategoriaModel> = listOf()
    var datos : MutableMap<String, String> = mutableMapOf()
    var generales: List<GeneralModel> = listOf()
}