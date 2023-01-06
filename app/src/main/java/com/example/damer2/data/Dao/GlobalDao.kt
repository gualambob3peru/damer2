package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.*

@Dao
interface GlobalDao {
    @Query("SELECT * from Global")
     fun getAll(): List<Global>

    @Query("SELECT * from Global WHERE id = :id")
     fun get(id: Int): Global

    @Query("SELECT * from Global WHERE codigo = :codigo")
    fun get_codigo(codigo: String): Global

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(global: Global)

    @Query("DELETE FROM Global")
     fun borrarTodo()
}