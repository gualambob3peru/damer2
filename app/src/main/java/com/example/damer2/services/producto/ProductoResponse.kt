package com.example.damer2.services.medicion

import com.example.damer2.data.Entities.Producto
import com.example.damer2.models.MedicionModel
import com.example.damer2.models.PersonalModel
import com.example.damer2.models.ProductoModel
import com.google.gson.annotations.SerializedName

data class ProductoResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("body") var body: List<ProductoModel>,
    @SerializedName("messages") var message: String
)

