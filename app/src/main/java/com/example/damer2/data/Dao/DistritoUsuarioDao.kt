package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.*

@Dao
interface DistritoUsuarioDao {
    @Query("SELECT * from DistritoUsuario")
     fun getAll(): List<DistritoUsuario>

    @Query("SELECT * from DistritoUsuario WHERE id = :id")
     fun get(id: Int): DistritoUsuario

    @Query("SELECT count(1) from DistritoUsuario WHERE codigo = :codigo")
    fun get_x_codigo(codigo: String): Int

    @Query("SELECT * from DistritoUsuario WHERE cod_zona = :cod_zona")
     fun get_cod_zona(cod_zona: String): List<DistritoUsuario>

    @Query("SELECT * from DistritoUsuario WHERE descripcion = :descripcion")
     fun get_descripcion(descripcion: String): DistritoUsuario


    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(distrito: DistritoUsuario)

    @Query("DELETE FROM DistritoUsuario")
     fun borrarTodo()
}