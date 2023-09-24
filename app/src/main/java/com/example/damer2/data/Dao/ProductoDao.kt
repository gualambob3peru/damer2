package com.example.damer2.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.data.Entities.Producto
import com.example.damer2.data.Entities.Usuario
import java.util.*

@Dao
interface ProductoDao {
    @Query("SELECT * from Producto")
     fun getAll(): List<Producto>

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria AND sku!='' AND sku!='0' ORDER BY upper(descripcion) ASC")
     fun getAllProductosNego(cod_negocio:String , cod_categoria:String ): List<Producto>

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio  AND sku!='' and sku!='0' AND estado='1'  ORDER BY upper(descripcion) ASC")
     fun getAllProductos_negocio(cod_negocio:String ): List<Producto>
     
    @Query("SELECT * from Producto WHERE id = :id")
     fun get(id: Int): Producto

    @Query("SELECT * from Producto WHERE sku = :sku")
    fun get_by_codigo(sku: String): Producto

    @Query("SELECT count(1) from Producto WHERE cod_negocio = :cod_negocio  and cod_categoria=:cod_categoria  and estado=1 order by upper(descripcion) asc")
    fun getNegocio_x_categoria(cod_negocio:String, cod_categoria:String): Int

    @Query("SELECT count(1) from Producto WHERE cod_negocio = :cod_negocio AND cod_categoria=:cod_categoria AND sku = :sku")
    fun get_x_categoria_sku(cod_negocio:String,cod_categoria: String,sku: String): Int

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria  AND sku!='' AND sku!='0'  ORDER BY upper(descripcion) ASC")
     fun getAllProductos_categoria(cod_negocio:String , cod_categoria:String ): List<Producto>

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria  AND sku LIKE '%' || :sku || '%' ")
    fun getAllProductos_categoria_sku(cod_negocio:String , cod_categoria:String ,sku:String): List<Producto>

    @Query("SELECT id,cod_categoria as codigo,desc_categoria as descripcion,1 estado, 0 tipoDato from Producto WHERE cod_negocio = :cod_negocio and sku!='0' GROUP BY cod_categoria,desc_categoria")
     fun getCategorias_negocio(cod_negocio:String ): List<Categoria>

    @Query("SELECT count(1) from Producto WHERE cod_negocio = :cod_negocio AND sku!='' and sku!='0' AND (inventario = '' OR compra='' OR precio ='' OR vant='')")
    fun getNumCampoVacio_by_negocio(cod_negocio: String): Int

    @Query("SELECT count(1) from Producto WHERE estadoGuardado = 0 AND cod_negocio=:cod_negocio and sku!='0' and estado=1 and (estadoCambiado=1 or estadoCambiado=0)")
    fun getGuardados_by_negocio(cod_negocio: String): Int

    @Query("SELECT count(1) from Producto WHERE estadoEnviado = 1 and estado=1")
    fun getAuditados(): Int

    @Query("SELECT count(1) from Producto WHERE estado=1")
    fun getTotal(): Int

    @Query("SELECT count(1) from Producto WHERE cod_negocio = :cod_negocio and cod_categoria=:cod_categoria and cambioNegocio=3")
    fun getProducto_count_menosCategoria(cod_negocio: String,cod_categoria: String): Int

    @Query("SELECT count(1) from Producto WHERE cod_negocio = :cod_negocio and cod_categoria=:cod_categoria and estadoCambiado=:estadoCambiado")
    fun get_negocio_categoria_excluido(cod_negocio: String,cod_categoria: String,estadoCambiado:Int): Int

    @Query("SELECT count(1) from Producto WHERE cod_negocio = :cod_negocio and estadoCambiado=:estadoCambiado")
    fun get_negocio_excluido(cod_negocio: String,estadoCambiado:Int): Int

    @Query("SELECT * from Producto WHERE cod_negocio = :cod_negocio and cod_categoria=:cod_categoria")
    fun get_negocio_categoria(cod_negocio: String,cod_categoria: String): List<Producto>




    @Query("UPDATE Producto SET compra=:compra , inventario=:inventario,precio=:precio, ve=:ve WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria")
     fun update(cod_negocio:String , cod_categoria:String ,compra:String,inventario:String,precio:String,ve:String)

    @Query("UPDATE Producto SET  compra=:compra , inventario=:inventario,precio=:precio, ve=:ve WHERE sku=:sku AND cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria")
     fun update_sku(sku:String,cod_negocio:String , cod_categoria:String ,compra:String,inventario:String,precio:String,ve:String)

    @Query("UPDATE Producto SET  estadoEnviado = :estadoEnviado")
    fun update_estadoEnviado(estadoEnviado:Int )

    @Query("UPDATE Producto SET  estadoEnviado = :estadoEnviado where cod_negocio=:cod_negocio and sku!='0'")
    fun update_estadoEnviado_negocio(cod_negocio:String,estadoEnviado:Int )

    @Query("UPDATE Producto SET estadoCambiado=:estadoCambiado WHERE cod_negocio=:cod_negocio and cod_categoria=:cod_categoria")
    fun update_estadoExcluido(cod_negocio:String, cod_categoria:String, estadoCambiado: Int )

    @Query("UPDATE Producto SET estadoTemporal=:estadoTemporal WHERE cod_negocio=:cod_negocio ")
    fun update_estadoTemporal(cod_negocio:String, estadoTemporal: Int )

    @Query("UPDATE Producto SET cod_negocio=:cod_new_codigo, cambioNegocio=:cod_negocio WHERE cod_negocio=:cod_negocio and cod_categoria=:cod_categoria")
    fun update_negocio(cod_negocio:String, cod_categoria:String, cod_new_codigo: String )

    @Query("UPDATE Producto SET estadoGuardado=1 WHERE cod_negocio=:cod_negocio and cod_categoria=:cod_categoria")
    fun update_estadoGuardado(cod_negocio:String, cod_categoria:String )

    @Query("UPDATE Producto SET estadoGuardado=0 WHERE cod_negocio=:cod_negocio and cod_categoria=:cod_categoria")
    fun update_estadoNoGuardado(cod_negocio:String, cod_categoria:String )

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(producto: Producto)

    @Query("DELETE FROM Producto WHERE cod_negocio = :cod_negocio AND cod_categoria = :cod_categoria")
     fun borrarTodo(cod_negocio:String , cod_categoria:String )

    @Query("DELETE FROM Producto WHERE cod_negocio = :cod_negocio")
     fun borrarAll(cod_negocio:String )

    @Query("DELETE FROM Producto")
     fun borrarAllAll()
}