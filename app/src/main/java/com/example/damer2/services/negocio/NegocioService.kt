package com.example.damer2.services.negocio

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NegocioService {
    @Headers("Content-Type: application/json")
    @POST("negocio")
    fun negocio(@Body NegocioInput: NegocioInput): Call<NegocioResponse>

    @Headers("Content-Type: application/json")
    @POST("guardar")
    fun guardar(@Body NegocioInput: NegocioInput): Call<NegocioResponse>

    @Headers("Content-Type: application/json")
    @POST("categorias")
    fun categorias(@Body NegocioInput: NegocioInput): Call<NegocioCategoriaResponse>

    @Headers("Content-Type: application/json")
    @POST("get_x_email_distrito")
    fun get_x_email_distrito(@Body NegocioInput: NegocioEmailDistritoInput): Call<NegocioResponse>

    companion object {

        //var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/negocio/"
        var BASE_URL = "http://damer2.b3peru.com/api/negocio/"
        fun create() : NegocioService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(NegocioService::class.java)

        }
    }
}