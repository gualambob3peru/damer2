package com.example.damer2.services.todo

import com.example.damer2.shared.UsuarioApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.POST

interface TodoService {
    @Headers("Content-Type: application/json")
    @POST("get")
    fun get(): Call<TodoResponse>

    @POST("getMedicion")
    fun getMedicion(): Call<MedicionResponse>

    companion object {

       // var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/todo/"

        var BASE_URL = UsuarioApplication.prefs.getRutaApi() + "api/todo/"
        fun create() : TodoService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(TodoService::class.java)

        }
    }
}


