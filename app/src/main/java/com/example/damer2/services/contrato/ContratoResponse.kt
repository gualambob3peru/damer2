package com.example.damer2.services.contrato

import com.example.damer2.models.ContratoModel
import com.google.gson.annotations.SerializedName

data class ContratoResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("body") var body: List<ContratoModel>,
    @SerializedName("messages") var message: String
)

