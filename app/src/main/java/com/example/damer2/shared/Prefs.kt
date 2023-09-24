package com.example.damer2.shared

import android.content.Context

class Prefs(val context:Context) {
    val SHARED_NAME = "Mydtb"
    val ID_PERSONAL = ""
    val NOMBRES = ""
    val APELLIDOPATERNO = ""
    val APELLIDOMATERNO = ""
    val EMAIL = ""
    val USUARIO = "USUARIO"

    val storage = context.getSharedPreferences(SHARED_NAME,0)




    fun setFechaVisita(fecha_visita : String){
        storage.edit().putString("fecha_visita",fecha_visita).apply()
    }
    fun setRutaApi(ruta:String){
        storage.edit().putString("ruta_api",ruta).apply()
    }

    fun setCod_Negocio(cod_negocio:String){
        storage.edit().putString("cod_negocio_ele",cod_negocio).apply()
    }

    fun setUsuario (usuario : MutableMap<String,String>){
        storage.edit().putString("id",usuario["id"]).apply()
        storage.edit().putString("nombres",usuario["nombres"]).apply()
        storage.edit().putString("apellidoPaterno",usuario["apellidoPaterno"]).apply()
        storage.edit().putString("apellidoMaterno",usuario["apellidoMaterno"]).apply()
        storage.edit().putString("email",usuario["email"]).apply()
        storage.edit().putString("telefono",usuario["telefono"]).apply()
        storage.edit().putString("medicion",usuario["medicion"]).apply()
        storage.edit().putString("anio",usuario["anio"]).apply()
        storage.edit().putString("mes",usuario["mes"]).apply()
        storage.edit().putString("contrasena",usuario["contrasena"]).apply()

    }

    fun getUsuario(): MutableMap<String,String> {
        val usuario = emptyMap<String,String>().toMutableMap()

        usuario["id"] = storage.getString("id","")!!
        usuario["nombres"] = storage.getString("nombres","")!!
        usuario["apellidoPaterno"] = storage.getString("apellidoPaterno","")!!
        usuario["apellidoMaterno"] = storage.getString("apellidoMaterno","")!!
        usuario["password"] = storage.getString("password","")!!
        usuario["email"] = storage.getString("email","")!!
        usuario["telefono"] = storage.getString("telefono","")!!
        usuario["created_at"] = storage.getString("created_at","")!!
        usuario["idCargo"] = storage.getString("idCargo","")!!
        usuario["idTipoDocumento"] = storage.getString("idTipoDocumento","")!!
        usuario["nroDocumento"] = storage.getString("nroDocumento","")!!
        usuario["medicion"] = storage.getString("medicion","")!!
        usuario["anio"] = storage.getString("anio","")!!
        usuario["mes"] = storage.getString("mes","")!!

        return usuario
    }

    fun getFechaVisita ():String{
        return storage.getString("fecha_visita","")!!
    }

    fun getRutaApi(): String {
        return storage.getString("ruta_api","")!!
    }
    fun getCod_Negocio(): String {
        return storage.getString("cod_negocio_ele","")!!
    }
}

