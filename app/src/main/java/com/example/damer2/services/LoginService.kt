package com.example.damer2.services

import com.example.damer2.global.GlobalVar.Companion.RUTA_API
import com.example.damer2.shared.UsuarioApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun validar(@Body loginInput: LoginInput): Call<LoginResponse>


    companion object {

        //var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/login/"
        var BASE_URL = RUTA_API + "api/login/"
        fun create() : LoginService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RUTA_API + "api/login/")
                .build()
            return retrofit.create(LoginService::class.java)

        }
    }
}