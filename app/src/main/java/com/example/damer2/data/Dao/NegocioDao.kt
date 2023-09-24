package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.data.Entities.Usuario

@Dao
interface NegocioDao {
    @Query("SELECT * from Negocio where estadoVi=1 ORDER BY id DESC")
     fun getAll(): List<Negocio>

    @Query("SELECT * from Negocio WHERE codigo_negocio= :codigo LIMIT 1")
     fun get_codigo(codigo : String): Negocio

    @Query("SELECT * from Negocio WHERE id = :id")
     fun get(id: Int): Negocio

    @Query("SELECT * from Negocio WHERE distrito = :cod_distrito and estado=1 order by upper(descripcion) asc")
    fun get_distrito(cod_distrito: String): List<Negocio>




    @Query("SELECT * from Negocio WHERE distrito = :cod_distrito and estado=0 order by upper(descripcion) asc")
    fun get_archivados(cod_distrito: String): List<Negocio>

    @Query("SELECT * from Negocio WHERE estadoVi = 2")
     fun get_ocultos(): List<Negocio>

    @Query("SELECT * from Negocio WHERE canal = :cod_canal and distrito=:cod_distrito and estadoEnviado=0")
    fun getNegociosIncluir(cod_canal: String,cod_distrito: String): List<Negocio>


    @Query("SELECT estadoEnviado from Negocio WHERE codigo_negocio = :codigo_negocio")
    fun getEstadoEnviado(codigo_negocio: String): Int

    @Query("SELECT count(1) from Negocio WHERE estadoEnviado=1 and estado=1")
    fun getEnviados(): Int

    @Query("SELECT count(1) from Negocio WHERE estado=1")
    fun getTotal(): Int

    @Query("UPDATE Negocio SET estadoTemporal=:estadoTemporal WHERE codigo_negocio=:codigo_negocio ")
    fun update_estadoTemporal(codigo_negocio:String, estadoTemporal: Int )

    @Query("UPDATE  Negocio set estadoVi=1 WHERE codigo_negocio= :cod_negocio")
     fun update_recuperar(cod_negocio:String)

    @Query("UPDATE  Negocio set estado=0 WHERE codigo_negocio= :cod_negocio")
     fun update_archivar(cod_negocio:String)

    @Query("UPDATE  Negocio set estadoEnviado=:estadoEnviado WHERE codigo_negocio= :cod_negocio")
    fun update_estadoenviado(cod_negocio:String,estadoEnviado:Int)

    @Query("UPDATE  Negocio set estadoExcluido=:estadoExcluido WHERE codigo_negocio= :cod_negocio")
    fun update_estadoExcluido(cod_negocio:String,estadoExcluido:Int)
    @Query("UPDATE  Negocio set lat=:lat,lgn=:lgn WHERE codigo_negocio= :cod_negocio")
    fun update_location(cod_negocio:String,lat:String,lgn:String)

    @Query("UPDATE  Negocio set descripcion=:descripcion,nombre=:nombre,vendedor=:vendedor,telefono=:telefono,canal=:canal WHERE codigo_negocio= :codigo_negocio")
    fun update_varios(descripcion:String,nombre:String,vendedor:String,telefono:String,canal:String,codigo_negocio:String)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(negocio: Negocio)

    @Query("DELETE FROM Negocio")
     fun borrarTodo()
}