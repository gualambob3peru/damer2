package com.example.damer2.services.medicion

import com.example.damer2.data.Entities.Producto
import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

 data class ProductoInput (
     @SerializedName("codigo") var codigo: String
     )

