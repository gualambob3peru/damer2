package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Producto
import com.example.damer2.data.Entities.Usuario
import java.util.*

@Dao
interface ProductoDao {
    @Query("SELECT * from Producto")
     fun getAll(): List<Producto>

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria AND sku!=''")
     fun getAllProductosNego(cod_negocio:String , cod_categoria:String ): List<Producto>

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio  AND sku!=''")
     fun getAllProductos_negocio(cod_negocio:String ): List<Producto>
     
    @Query("SELECT * from Producto WHERE id = :id")
     fun get(id: Int): Producto

    @Query("SELECT count(1) from Producto WHERE cod_negocio = :cod_negocio AND cod_categoria=:cod_categoria AND sku = :sku")
    fun get_x_categoria_sku(cod_negocio:String,cod_categoria: String,sku: String): Int

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria  AND sku!=''")
     fun getAllProductos_categoria(cod_negocio:String , cod_categoria:String ): List<Producto>

    @Query("SELECT id,cod_categoria as codigo,desc_categoria as descripcion,1 estado from Producto WHERE cod_negocio = :cod_negocio GROUP BY cod_categoria,desc_categoria")
     fun getCategorias_negocio(cod_negocio:String ): List<Categoria>

    @Query("UPDATE Producto SET compra=:compra , inventario=:inventario,precio=:precio, ve=:ve WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria")
     fun update(cod_negocio:String , cod_categoria:String ,compra:String,inventario:String,precio:String,ve:String)

    @Query("UPDATE Producto SET  compra=:compra , inventario=:inventario,precio=:precio, ve=:ve WHERE sku=:sku AND cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria")
     fun update_sku(sku:String,cod_negocio:String , cod_categoria:String ,compra:String,inventario:String,precio:String,ve:String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(producto: Producto)

    @Query("DELETE FROM Producto WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria")
     fun borrarTodo(cod_negocio:String , cod_categoria:String )

    @Query("DELETE FROM Producto WHERE cod_negocio = :cod_negocio")
     fun borrarAll(cod_negocio:String )

    @Query("DELETE FROM Producto")
     fun borrarAllAll()
}