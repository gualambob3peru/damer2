package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Producto
import com.example.damer2.data.Entities.ProductoMaster
import com.example.damer2.data.Entities.Usuario
import java.util.*

@Dao
interface ProductoMasterDao {
    @Query("SELECT * from ProductoMaster")
     fun getAll(): List<ProductoMaster>

    @Query("SELECT * from ProductoMaster WHERE id = :id")
    fun get(id: Int): ProductoMaster

    @Query("SELECT count(*) from ProductoMaster")
     fun getCount(): Int

    @Query("SELECT * from ProductoMaster WHERE sku = :sku")
     fun getsku(sku:String): ProductoMaster

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(producto: ProductoMaster)

    @Query("DELETE FROM ProductoMaster")
     fun borrarAllAll()
}