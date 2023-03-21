package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Canal
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Usuario
import com.example.damer2.data.Entities.Zona

@Dao
interface CanalDao {
    @Query("SELECT * from Canal order by descripcion asc")
     fun getAll(): List<Canal>

    @Query("SELECT * from Canal WHERE id = :id")
     fun get(id: Int): Canal

     @Query("SELECT * from Canal WHERE codigo = :codigo")
     fun get_codigo(codigo: String): Canal

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(canal: Canal)

    @Query("DELETE FROM Canal")
     fun borrarTodo()
}