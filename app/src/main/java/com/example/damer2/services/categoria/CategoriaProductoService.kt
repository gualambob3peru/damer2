package com.example.damer2.services.medicion

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CategoriaProductoService {
    @Headers("Content-Type: application/json")
    @POST("productos")
    fun productos(@Body CategoriaProductoInput: CategoriaProductoInput): Call<CategoriaProductoResponse>


    companion object {

        //var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/categoriaApi/"
        var BASE_URL = "http://damer2.b3peru.com/api/categoriaApi/"
        fun create() : CategoriaProductoService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(CategoriaProductoService::class.java)

        }
    }
}