package com.example.damer2.services.distrito

import com.example.damer2.models.DistritoModel
import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

data class DistritoResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("body") var body: List<DistritoModel>,
    @SerializedName("messages") var message: String
)

