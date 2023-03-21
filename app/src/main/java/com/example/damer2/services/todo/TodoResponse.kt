package com.example.damer2.services.todo

import com.example.damer2.models.MedicionModel
import com.example.damer2.models.PersonalModel
import com.example.damer2.models.TodoModel
import com.google.gson.annotations.SerializedName

data class TodoResponse(
    @SerializedName("status") var status: String,
    @SerializedName("error") var error: String,
    @SerializedName("body") var body: TodoModel,
    @SerializedName("messages") var message: String
)

