package com.example.damer2

import android.content.Intent
import android.graphics.Color
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
import com.example.damer2.R
import com.example.damer2.adapter.ProductoAdapter
import com.example.damer2.adapter.ProductoAgregarSkuAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Contrato
import com.example.damer2.data.Entities.Producto
import com.example.damer2.data.Entities.ProductoMaster
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoAgregarSkuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_agregar_sku)

        val db = AuditoriaDb(this)

        var cod_negocio = intent.getStringExtra("cod_negocio").toString()
        var direccion = intent.getStringExtra("direccion").toString()
        var cod_categoria = intent.getStringExtra("cod_categoria").toString()
        var ca_descripcion = intent.getStringExtra("ca_descripcion").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()
        var cod_canal = intent.getStringExtra("cod_canal").toString()

        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        val textView2 = findViewById<TextView>(R.id.txtMedicion)
        textView2.text = "MEDICION : "+ medicion.toString()

        val producto_agregar_btnGuardarCodigo = findViewById<Button>(R.id.producto_agregar_btnGuardarCodigo)
        val producto_agregar_inputBuscarCodigo = findViewById<EditText>(R.id.producto_agregar_inputBuscarCodigo)
        val producto_agregar_tDescripcion_codigo = findViewById<TextView>(R.id.producto_agregar_tDescripcion_codigo)
        val producto_agregar_btnBuscarNombre = findViewById<Button>(R.id.producto_agregar_btnBuscarNombre)
        val producto_agregar_inputBuscarNombre = findViewById<EditText>(R.id.producto_agregar_inputBuscarNombre)
        val producto_agregar_btnBuscarCodigo = findViewById<Button>(R.id.producto_agregar_btnBuscarCodigo)
        val producto_agregar_tDescripcion_nombre = findViewById<TextView>(R.id.producto_agregar_tDescripcion_nombre)
        val producto_agregar_tSkuGuardar = findViewById<TextView>(R.id.producto_agregar_tSkuGuardar)
        val btnAtras = findViewById<ImageView>(R.id.btnAtras)

        var adapter = ProductoAgregarSkuAdapter()
        var skuBuscado = ""
        var arr_skus = mutableListOf<String>()


        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProductoAgregar)
        recyclerView.layoutManager = linearLayoutManager

        producto_agregar_btnBuscarCodigo.setOnClickListener {
            arr_skus = mutableListOf<String>()
            lifecycleScope.launch(Dispatchers.IO){
                var codigo_sku = producto_agregar_inputBuscarCodigo.text.toString()
                var producto = db.ProductoMasterDao().getsku_categoria(codigo_sku,cod_categoria)
                var productoUsado = db.ProductoDao().get_x_categoria_sku(cod_negocio,cod_categoria,codigo_sku)

                runOnUiThread {
                    if(productoUsado>0){
                        producto_agregar_tDescripcion_codigo.text = "Este SKU ya se esta utilizando"
                    }else{



                        if(producto != null){
                            producto_agregar_tDescripcion_codigo.text = producto.descripcion
                            skuBuscado = codigo_sku
                            arr_skus.add(skuBuscado)
                            producto_agregar_tSkuGuardar.text = codigo_sku



                        }else{
                            producto_agregar_tDescripcion_codigo.text = "SKU no válido"
                            skuBuscado = ""
                            producto_agregar_tSkuGuardar.text = "---"
                        }
                    }

                }
            }
        }

        producto_agregar_btnGuardarCodigo.setOnClickListener {
            if(arr_skus.size==0){
                Toast.makeText(applicationContext,"No hay SKUs para guardar", Toast.LENGTH_SHORT).show()
            }else{
                lifecycleScope.launch(Dispatchers.IO){
                    for ( sku in arr_skus){
                        var producto = db.ProductoMasterDao().getsku(sku)
                        var miContrato = db.ContratoDao().get_contrato(cod_categoria,cod_zona,cod_canal)
                        if(miContrato == null) miContrato= Contrato()
                        //Verificando variables
                        var variables = miContrato.variables
                        if(variables == ""){
                            variables = "1,2,3,4"
                        }
                        var arr_variables= variables.split(",")

                        var compra = "-"
                        var inventario = "-"
                        var precio = "-"
                        var ve = "-"

                        for (varia in arr_variables){
                            if(varia=="1"){
                                compra=""
                            }else if(varia=="2"){
                                inventario=""
                            }else if(varia=="3"){
                                precio=""
                            }else if(varia=="4"){
                                ve=""
                            }
                        }




                        db.ProductoDao().insert(
                            Producto(
                                0,
                                sku,
                                producto.cod_categoria,
                                cod_negocio,
                                producto.descripcion,
                                compra,
                                inventario,
                                precio,
                                ve,
                                "1",
                                ca_descripcion,
                                "0",
                                "",
                                "",
                                "",
                                "1",
                                "0"
                            )
                        )
                    }

                    finish()

                }

            }
        }


        producto_agregar_btnBuscarNombre.setOnClickListener {
            arr_skus = mutableListOf<String>()
            lifecycleScope.launch(Dispatchers.IO){
                var nombreSku = producto_agregar_inputBuscarNombre.text.toString()
                var productos = db.ProductoMasterDao().get_x_nombre_categoria(nombreSku,cod_categoria)

                if(productos !=null){
                    var arr_codigo : MutableList<String> = mutableListOf()
                    var arr_descripcion : MutableList<String> = mutableListOf()
                    var arr_producto : MutableList<ProductoMaster> = mutableListOf()

                    for(ind in productos.indices){

                        var productoUsado = db.ProductoDao().get_x_categoria_sku(cod_negocio,cod_categoria,productos[ind].sku)

                        //Poniendo solo los que no estan usados
                        if(productoUsado==0){
                            arr_codigo.add(productos[ind].sku)
                            arr_descripcion.add(productos[ind].descripcion)
                            var n_producto= ProductoMaster(
                                0,
                                productos[ind].sku,
                                productos[ind].descripcion
                            )
                            arr_producto.add(n_producto)
                        }
                    }

                    adapter.onItemClick = { producto,divcodigo,divdescripcion->
                        skuBuscado = producto.sku


                        runOnUiThread {
                            if(arr_skus.contains(skuBuscado)){
                                arr_skus.remove(skuBuscado)
                                producto_agregar_tSkuGuardar.text = skuBuscado
                                divcodigo.setBackgroundResource(R.color.azul_1)
                                divdescripcion.setBackgroundResource(R.color.azul_1)
                            }else{
                                arr_skus.add(skuBuscado)
                                producto_agregar_tSkuGuardar.text = skuBuscado
                                divcodigo.setBackgroundResource(R.color.amarillo_1)
                                divdescripcion.setBackgroundResource(R.color.amarillo_1)
                            }
                        }
                    }

                    runOnUiThread {
                        adapter.setList(arr_codigo,arr_descripcion,arr_producto)
                        recyclerView.adapter = adapter
                        producto_agregar_tDescripcion_nombre.text = ""
                    }
                }else{
                    runOnUiThread {
                        producto_agregar_tDescripcion_nombre.text = "No se encontró resultados"
                    }

                }


            }
        }

        btnAtras.setOnClickListener{
            finish()
        }
    }
}