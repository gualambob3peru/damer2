package com.example.damer2.services

import com.example.damer2.models.PersonalModel
import com.google.gson.annotations.SerializedName

 data class LoginInput (
     @SerializedName("email") var email: String,
     @SerializedName("password") var password: String
     )

