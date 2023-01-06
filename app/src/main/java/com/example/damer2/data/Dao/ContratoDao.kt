package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Contrato


@Dao
interface ContratoDao {
    @Query("SELECT * from Contrato")
     fun getAll(): List<Contrato>

    @Query("SELECT * from Contrato WHERE id = :id")
     fun get(id: Int): Contrato

    @Query("SELECT * from Contrato WHERE cod_categoria = :cod_categoria and cod_zona = :cod_zona and cod_canal = :cod_canal")
    fun get_contrato(cod_categoria: String,cod_zona:String,cod_canal:String): Contrato

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(contrato: Contrato)

    @Query("DELETE FROM Contrato")
     fun borrarTodo()
}