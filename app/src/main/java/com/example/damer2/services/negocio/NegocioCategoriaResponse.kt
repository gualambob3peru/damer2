package com.example.damer2.services.negocio

import com.example.damer2.models.CategoriaModel
import com.example.damer2.models.MedicionModel
import com.example.damer2.models.NegocioModel
import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

data class NegocioCategoriaResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("body") var body: List<CategoriaModel>,
    @SerializedName("messages") var message: String
)

