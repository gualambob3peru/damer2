package com.example.damer2.services.negocio

import com.example.damer2.global.GlobalVar.Companion.RUTA_API
import com.example.damer2.shared.UsuarioApplication
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

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

    @Headers("Content-Type: application/json")
    @POST("subirLocalizacionNegocio")
    fun subirLocalizacionNegocio(@Body NegocioLocalizacionInput: NegocioLocalizacionInput): Call<NegocioResponse>

    companion object {

        //var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/negocio/"
        var BASE_URL = RUTA_API + "api/negocio/"
        fun create() : NegocioService {
            val client = OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS) // Increase the connection timeout to 30 seconds
                .readTimeout(300, TimeUnit.SECONDS)    // Increase the read timeout to 30 seconds
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RUTA_API + "api/negocio/")
                .client(client)
                .build()
            return retrofit.create(NegocioService::class.java)

        }
    }
}