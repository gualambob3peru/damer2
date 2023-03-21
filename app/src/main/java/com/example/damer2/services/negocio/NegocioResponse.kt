package com.example.damer2.services.negocio

import com.example.damer2.models.MedicionModel
import com.example.damer2.models.NegocioModel
import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

data class NegocioResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("body") var body: List<NegocioModel>,
    @SerializedName("messages") var messages: String
)

