package com.example.damer2.services.medicion

import com.example.damer2.data.Entities.Producto
import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName
import java.util.*

data class ProductoSubirInput (
     @SerializedName("productos") var productos: List<Producto>,
     @SerializedName("datosSubida") var datosSubida: MutableMap<String,String>
     )

