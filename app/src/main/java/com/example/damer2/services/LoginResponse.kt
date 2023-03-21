package com.example.damer2.services

import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("user") var user: List<PersonalModel>,
    @SerializedName("messages") var message: String
)

