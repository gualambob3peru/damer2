package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.*

@Dao
interface DistritoDao {
    @Query("SELECT * from Distrito")
     fun getAll(): List<Distrito>

    @Query("SELECT * from Distrito WHERE id = :id")
     fun get(id: Int): Distrito

    @Query("SELECT * from Distrito WHERE cod_zona = :cod_zona")
     fun get_cod_zona(cod_zona: String): List<Distrito>

    @Query("SELECT * from Distrito WHERE descripcion = :descripcion")
     fun get_descripcion(descripcion: String): Distrito


    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(distrito: Distrito)

    @Query("DELETE FROM Distrito")
     fun borrarTodo()
}