package com.example.damer2.services.medicion

import com.example.damer2.global.GlobalVar.Companion.RUTA_API
import com.example.damer2.shared.UsuarioApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CategoriaService {
    @Headers("Content-Type: application/json")
    @POST("medicion")
    fun medicion(@Body MedicionInput: CategoriaInput): Call<CategoriaResponse>

    @Headers("Content-Type: application/json")
    @POST("categorias")
    fun categorias(): Call<CategoriaResponse>


    companion object {

        // var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/categoriaApi/"
        var BASE_URL = RUTA_API + "api/categoriaApi/"
        fun create() : CategoriaService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RUTA_API + "api/categoriaApi/")
                .build()
            return retrofit.create(CategoriaService::class.java)

        }
    }
}