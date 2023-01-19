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

    @Query("SELECT * from Negocio WHERE distrito = :cod_distrito order by descripcion asc")
    fun get_distrito(cod_distrito: String): List<Negocio>

    @Query("SELECT * from Negocio WHERE estadoVi = 2")
     fun get_ocultos(): List<Negocio>

    @Query("SELECT estadoEnviado from Negocio WHERE codigo_negocio = :codigo_negocio")
    fun getEstadoEnviado(codigo_negocio: String): Int

    @Query("UPDATE  Negocio set estadoVi=1 WHERE codigo_negocio= :cod_negocio")
     fun update_recuperar(cod_negocio:String)

    @Query("UPDATE  Negocio set estadoVi=2 WHERE codigo_negocio= :cod_negocio")
     fun update_archivar(cod_negocio:String)

    @Query("UPDATE  Negocio set estadoEnviado=:estadoEnviado WHERE codigo_negocio= :cod_negocio")
    fun update_estadoenviado(cod_negocio:String,estadoEnviado:Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(negocio: Negocio)

    @Query("DELETE FROM Negocio")
     fun borrarTodo()
}