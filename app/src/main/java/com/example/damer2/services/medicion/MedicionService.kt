package com.example.damer2.services.medicion

import com.example.damer2.global.GlobalVar.Companion.RUTA_API
import com.example.damer2.shared.UsuarioApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MedicionService {
    @Headers("Content-Type: application/json")
    @POST("medicion")
    fun medicion(): Call<CategoriaResponse>


    companion object {

        var BASE_URL = RUTA_API + "api/medicion/"

        fun create() : CategoriaService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RUTA_API + "api/medicion/")
                .build()
            return retrofit.create(CategoriaService::class.java)

        }
    }
}