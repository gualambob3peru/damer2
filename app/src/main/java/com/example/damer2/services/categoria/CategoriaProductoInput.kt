package com.example.damer2.services.medicion

import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

 data class CategoriaProductoInput (
     @SerializedName("cod_categoria") var cod_categoria: String,
     @SerializedName("cod_negocio") var cod_negocio: String
     )

