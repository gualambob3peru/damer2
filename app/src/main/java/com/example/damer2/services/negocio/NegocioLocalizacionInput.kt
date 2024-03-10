package com.example.damer2.services.negocio

import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

 data class NegocioLocalizacionInput (
     @SerializedName("cod_negocio") var cod_negocio: String,
     @SerializedName("lat") var lat: String,
     @SerializedName("lgn") var lgn: String,
     )

