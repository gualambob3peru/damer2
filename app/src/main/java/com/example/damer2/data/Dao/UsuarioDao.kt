package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * from Usuario")
     fun getAll(): List<Usuario>

    @Query("SELECT * from Usuario WHERE id = :id")
     fun get(id: Int): Usuario

    @Query("SELECT * from Usuario WHERE email = :email")
     fun get_email(email: String): Usuario

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(usuario: List<Usuario>)

    @Query("DELETE FROM Usuario")
     fun borrarTodo()
}