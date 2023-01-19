package com.example.damer2.services.imagenService

import com.example.damer2.models.MedicionModel
import com.example.damer2.models.NegocioModel
import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

data class ImageApiResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("body") var body: String,
    @SerializedName("messages") var messages: String
)

