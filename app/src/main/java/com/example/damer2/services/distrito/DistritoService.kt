package com.example.damer2.services.distrito

import com.example.damer2.shared.UsuarioApplication
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DistritoService {
    @Headers("Content-Type: application/json")
    @POST("get_x_auditor")
    fun get_x_auditor(@Body distritoEmailInput: DistritoEmailInput): Call<DistritoResponse>


    companion object {

        //var BASE_URL = "http://192.168.3.5/auditoria/public_auditoria/auditoria/api/login/"
        var BASE_URL = UsuarioApplication.prefs.getRutaApi() + "api/distritoApi/"
        fun create() : DistritoService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(DistritoService::class.java)

        }
    }
}