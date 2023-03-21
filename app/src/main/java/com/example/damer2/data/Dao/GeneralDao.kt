package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.General
import com.example.damer2.data.Entities.Usuario
import com.example.damer2.data.Entities.Zona

@Dao
interface GeneralDao {
    @Query("SELECT * from General")
     fun getAll(): List<General>

    @Query("SELECT * from General WHERE id = :id")
     fun get(id: Int): General

    @Query("SELECT * from General WHERE parametro = :parametro")
    fun get_parametro(parametro : String): General

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(general: General)

    @Query("DELETE FROM General")
     fun borrarTodo()
}