package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Usuario
import com.example.damer2.data.Entities.Zona

@Dao
interface ZonaDao {
    @Query("SELECT * from Zona")
     fun getAll(): List<Zona>

    @Query("SELECT * from Zona WHERE id = :id")
     fun get(id: Int): Zona

    @Query("SELECT * from Zona WHERE descripcion = :descripcion")
     fun get_descripcion(descripcion: String): Zona


    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(zona: Zona)

    @Query("DELETE FROM Zona")
     fun borrarTodo()
}