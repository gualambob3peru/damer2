package com.example.damer2.services.medicion

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

        var BASE_URL = UsuarioApplication.prefs.getRutaApi() + "api/medicion/"

        fun create() : CategoriaService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(CategoriaService::class.java)

        }
    }
}