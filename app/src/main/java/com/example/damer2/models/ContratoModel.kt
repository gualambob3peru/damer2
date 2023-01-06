package com.example.damer2.models

import com.google.gson.annotations.SerializedName

class ContratoModel {
    var id: String = ""
    var cod_categoria: String = ""
    var cod_zona: String = ""
    var cod_canal: String = ""
    var variables : List<Int> = listOf()
}