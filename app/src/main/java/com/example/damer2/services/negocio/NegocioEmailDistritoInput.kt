package com.example.damer2.services.negocio

import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

 data class NegocioEmailDistritoInput (
     @SerializedName("email") var email: String,
     @SerializedName("cod_distrito") var cod_distrito: String
     )

