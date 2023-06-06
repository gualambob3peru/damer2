package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Usuario

@Dao
interface CategoriaDao {
    @Query("SELECT * from Categoria order by descripcion asc")
     fun getAll(): List<Categoria>


    @Query("SELECT * from Categoria WHERE id = :id")
     fun get(id: Int): Categoria

    @Query("SELECT * from Categoria WHERE codigo = :codigo")
    fun get_by_codigo(codigo: String): Categoria

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(categoria: Categoria)

    @Query("DELETE FROM Categoria")
     fun borrarTodo()
}