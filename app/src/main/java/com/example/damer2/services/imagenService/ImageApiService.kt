package com.example.damer2.services.imagenService

import com.example.damer2.global.GlobalVar.Companion.RUTA_API
import com.example.damer2.services.medicion.CategoriaService
import com.example.damer2.shared.UsuarioApplication
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ImageApiService {

    @Multipart
    @POST("upload_image")
    fun uploadFile(@Part file: MultipartBody.Part,
                           @Part("cod_sku")  cod_sku:Int,
                           @Part ("cod_negocio") cod_negocio:Int): Call<ImageApiResponse>

    companion object {

        var BASE_URL = RUTA_API + "api/image/"

        fun create() : ImageApiService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RUTA_API + "api/image/")
                .build()
            return retrofit.create(ImageApiService::class.java)

        }
    }

}