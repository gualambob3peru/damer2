package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.ProductoAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Producto
import com.example.damer2.ProductoAgregarSkuActivity
import com.example.damer2.data.Entities.Contrato
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoActivity : AppCompatActivity() {
    companion object {
        var listSkus = Array(1) {Array(8) {""} }

    }
    var num = 1
    override fun onResume() {
        super.onResume()

        if(num!=1){
            finish()
            val productoActivity = Intent(this, ProductoActivity::class.java)
            var cod_negocio = intent.getStringExtra("cod_negocio").toString()
            var direccion = intent.getStringExtra("direccion").toString()
            var cod_categoria = intent.getStringExtra("cod_categoria").toString()
            var ca_descripcion = intent.getStringExtra("ca_descripcion").toString()
            var cod_zona = intent.getStringExtra("cod_zona").toString()
            var cod_canal = intent.getStringExtra("cod_canal").toString()

            productoActivity.putExtra("cod_negocio", cod_negocio)
            productoActivity.putExtra("direccion", direccion)
            productoActivity.putExtra("cod_categoria", cod_categoria)
            productoActivity.putExtra("cod_zona", cod_zona)
            productoActivity.putExtra("cod_canal", cod_canal)
            productoActivity.putExtra("ca_descripcion", ca_descripcion)
            startActivity(productoActivity)
            //finish()
        }else{
            num++
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)


        val db = AuditoriaDb(this)
        var cod_negocio = intent.getStringExtra("cod_negocio").toString()
        var direccion = intent.getStringExtra("direccion").toString()
        var cod_categoria = intent.getStringExtra("cod_categoria").toString()
        var ca_descripcion = intent.getStringExtra("ca_descripcion").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()
        var cod_canal = intent.getStringExtra("cod_canal").toString()

        var producto_agregarProductoSku = findViewById<ImageView>(R.id.producto_agregarProductoSku)
        var producto_agregarProducto = findViewById<ImageView>(R.id.producto_agregarProducto)
        var btnAtras = findViewById<ImageView>(R.id.btnAtras)
        var producto_btnGuardar = findViewById<ImageView>(R.id.producto_btnGuardar)
        var producto_btnCeros = findViewById<ImageView>(R.id.producto_btnCeros)
        var producto_buscador = findViewById<EditText>(R.id.producto_buscador)
        var producto_btnBuscar = findViewById<Button>(R.id.producto_btnBuscar)
        var producto_btnCancelar = findViewById<Button>(R.id.producto_btnCancelar)


        val productoAgregarSkuActivity = Intent(this, ProductoAgregarSkuActivity::class.java)
        val productoAgregarActivity = Intent(this, ProductoAgregarActivity::class.java)
        val categoriaActivity = Intent(this, CategoriaActivity::class.java)

        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProducto)
        recyclerView.layoutManager = linearLayoutManager
        var adapter = ProductoAdapter()

        lifecycleScope.launch(Dispatchers.IO){
            var productos : List<Producto>

            productos = db.ProductoDao().getAllProductos_categoria(cod_negocio,cod_categoria)

            if(productos.size !=0){
                var arr_codigo : MutableList<String> = mutableListOf()
                var arr_descripcion : MutableList<String> = mutableListOf()
                var arr_producto : MutableList<Producto> = mutableListOf()

                listSkus = Array(productos.size) {Array(8) {""} }

                var misProductos = db.ProductoDao().getAllProductosNego(cod_negocio,cod_categoria)
                //var misProductos = productos
                for(ind in misProductos.indices){
                    listSkus[ind][0] = misProductos[ind].compra
                    listSkus[ind][1] = misProductos[ind].inventario
                    listSkus[ind][2] = misProductos[ind].precio
                    listSkus[ind][3] = misProductos[ind].ve
                    listSkus[ind][5] = misProductos[ind].descripcion
                    listSkus[ind][6] = misProductos[ind].vant
                    listSkus[ind][7] = misProductos[ind].compra_ant
                }


                for(ind in productos.indices){
                    arr_codigo.add(productos[ind].sku)
                    arr_descripcion.add(productos[ind].descripcion)
                    var n_producto= Producto(
                        productos[ind].id.toInt(),
                        productos[ind].sku,
                        "cate",
                        "nego",
                        productos[ind].descripcion,
                        listSkus[ind][0] ,
                        listSkus[ind][1],
                        listSkus[ind][2] ,
                        listSkus[ind][3] ,
                        "",
                        "",
                        productos[ind].vant,
                        "",
                        "",
                        "",
                        "",
                        productos[ind].compra_ant,
                    )

                    arr_producto.add(n_producto)

                }

                for(ind in listSkus.indices){
                    listSkus[ind][4] = productos[ind].sku
                }

                //Lista de contratos
                var miContrato = db.ContratoDao().get_contrato(cod_categoria,cod_zona,cod_canal)
                if(miContrato == null) miContrato= Contrato()
                val tipoDato = db.CategoriaDao().get_by_codigo(cod_categoria).tipoDato

                adapter.setList(arr_codigo,arr_descripcion,arr_producto,miContrato,tipoDato)

                adapter.onCompraKey = { position, variable, itemCompra,itemInventario, inv_ant,itemMsg ->
                    listSkus[position][variable] = itemCompra.text.toString()

                    var compra = itemCompra.text.toString()
                    var compraV = 0.0
                    if(compra.toDoubleOrNull()!=null){
                        if(compra=="") compraV = 0.0 else compraV = compra.toDouble()
                    }else{
                        if(compra=="") compraV = 0.0
                    }

                    var inv = itemInventario.text.toString()

                    var invV = 0.0

                    if(inv.toDoubleOrNull()!=null){
                        if(inv=="") invV = 0.0 else invV = inv.toDouble()
                    }else{
                        if(inv=="") invV = 0.0
                    }

                    var msg = ""

                    if(inv_ant + compraV - invV < 0){
                        msg = "Venta Negativa"
                    }

                    runOnUiThread {
                        itemMsg.text = msg
                    }
                }
                adapter.onInventarioKey = { position, variable, itemCompra,itemInventario, inv_ant,itemMsg->
                    listSkus[position][variable] = itemInventario.text.toString()

                    var compra = itemCompra.text.toString()
                    var compraV = 0.0
                    if(compra.toDoubleOrNull()!=null){
                        if(compra=="") compraV = 0.0 else compraV = compra.toDouble()
                    }else{
                        if(compra=="") compraV = 0.0
                    }

                    var inv = itemInventario.text.toString()

                    var invV = 0.0

                    if(inv.toDoubleOrNull()!=null){
                        if(inv=="") invV = 0.0 else invV = inv.toDouble()
                    }else{
                        if(inv=="") invV = 0.0
                    }

                    var msg = ""

                    if(inv_ant + compraV - invV < 0){
                        msg = "Venta Negativa"
                    }

                    runOnUiThread {
                        itemMsg.text = msg
                    }
                }
                adapter.onPrecioKey = { position, variable, itemPrecio ->
                    listSkus[position][variable] = itemPrecio.text.toString()
                }
                adapter.onVeKey = { position, variable, itemVe ->
                    listSkus[position][variable] = itemVe.text.toString()
                }
                runOnUiThread {
                    recyclerView.adapter = adapter

                }


            }else{
                //No tiene ninguna categoria
            }
        }

        producto_btnBuscar.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){

                    val texto = producto_buscador.text.toString()
                    var productos = db.ProductoDao().getAllProductos_categoria(cod_negocio,cod_categoria)

                    for(i in productos.indices){
                        if(productos[i].sku == texto ){
                            runOnUiThread {

                                recyclerView.scrollToPosition(i)

                            }
                            break
                        }
                    }


            }

        }

        producto_btnCancelar.setOnClickListener {
            runOnUiThread {
                recyclerView.scrollToPosition(0)
            }

        }

        producto_agregarProductoSku.setOnClickListener {
            runOnUiThread {
                productoAgregarSkuActivity.putExtra("cod_negocio", cod_negocio)
                productoAgregarSkuActivity.putExtra("direccion", direccion)
                productoAgregarSkuActivity.putExtra("cod_categoria", cod_categoria)
                productoAgregarSkuActivity.putExtra("ca_descripcion", ca_descripcion)
                productoAgregarSkuActivity.putExtra("cod_zona", cod_zona)
                productoAgregarSkuActivity.putExtra("cod_canal", cod_canal)
                startActivity(productoAgregarSkuActivity)
            }
        }

        producto_agregarProducto.setOnClickListener {
            runOnUiThread {
                productoAgregarActivity.putExtra("cod_negocio", cod_negocio)
                productoAgregarActivity.putExtra("direccion", direccion)
                productoAgregarActivity.putExtra("cod_categoria", cod_categoria)
                productoAgregarActivity.putExtra("ca_descripcion", ca_descripcion)
                productoAgregarActivity.putExtra("cod_zona", cod_zona)
                productoAgregarActivity.putExtra("cod_canal", cod_canal)
                startActivity(productoAgregarActivity)
            }
        }

        btnAtras.setOnClickListener {
            finish()
            /*runOnUiThread {
                categoriaActivity.putExtra("cod_negocio", cod_negocio)
                categoriaActivity.putExtra("direccion", direccion)
                startActivity(categoriaActivity)
            }*/
        }

        producto_btnGuardar.setOnClickListener {
            lifecycleScope.launch (Dispatchers.IO) {
                //db.ProductoDao().borrarTodo(intent.getStringExtra("cod_negocio").toString(),intent.getStringExtra("cod_categoria").toString())
                for(skus in listSkus){
                    db.ProductoDao().update_sku(
                        skus[4].toString(),
                        intent.getStringExtra("cod_negocio").toString(),
                        intent.getStringExtra("cod_categoria").toString(),
                        skus[0].toString(),
                        skus[1].toString(),
                        skus[2].toString(),
                        skus[3].toString()
                    );
                }

                runOnUiThread {
                    Toast.makeText(applicationContext,"Productos Guardados", Toast.LENGTH_SHORT).show()
                }
            }
        }

        producto_btnCeros.setOnClickListener {
            runOnUiThread {
               for(i in listSkus.indices ){
                    if(listSkus[i][0] == ""){
                        listSkus[i][0] = "0"
                    }
                    if(listSkus[i][1] == ""){
                        listSkus[i][1] = "0"
                    }
                    if(listSkus[i][2] == ""){
                        listSkus[i][2] = "0"
                    }
                    if(listSkus[i][3] == ""){
                        listSkus[i][3] = "0"
                    }
               }

                val miSize = listSkus.size - 1

                for(i in 0..miSize){
                    val miCompra = recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<EditText>(R.id.inputCompra)
                    val miInventario = recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<EditText>(R.id.inputInventario)
                    val miPrecio = recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<EditText>(R.id.inputPrecio)
                    val miVe = recyclerView.findViewHolderForAdapterPosition(i)?.itemView?.findViewById<EditText>(R.id.inputVe)

                    if(miCompra!=null && miCompra.text.toString()==""){
                        miCompra.setText("0")
                    }

                    if(miInventario!=null && miInventario.text.toString()==""){
                        miInventario.setText("0")
                    }

                    if(miPrecio!=null && miPrecio.text.toString()==""){
                        miPrecio.setText("0")
                    }

                    if(miVe!=null && miVe.text.toString()==""){
                        miVe.setText("0")
                    }

                }

                Toast.makeText(applicationContext,"Se cambió a ceros", Toast.LENGTH_SHORT).show()

            }
        }
    }



}