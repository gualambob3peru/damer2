package com.example.damer2.services.medicion

import com.example.damer2.shared.UsuarioApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ProductoService {
    @Headers("Content-Type: application/json")
    @POST("getAll")
    fun getAll(): Call<ProductoResponse>

    @Headers("Content-Type: application/json")
    @POST("subir")
    fun subir(@Body ProductoSubirInput: ProductoSubirInput): Call<ProductoSubirResponse>

    companion object {

        //var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/producto/"
        var BASE_URL = UsuarioApplication.prefs.getRutaApi() + "api/producto/"
        fun create() : ProductoService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ProductoService::class.java)

        }
    }
}