package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoAgregarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_agregar)

        val db = AuditoriaDb(this)

        var cod_negocio = intent.getStringExtra("cod_negocio").toString()
        var direccion = intent.getStringExtra("direccion").toString()
        var cod_categoria = intent.getStringExtra("cod_categoria").toString()
        var ca_descripcion = intent.getStringExtra("ca_descripcion").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()
        var cod_canal = intent.getStringExtra("cod_canal").toString()

        val producto_agregarProducto = findViewById<ImageView>(R.id.producto_agregarProducto)
        val producto_agregar_inputBuscarCodigo = findViewById<EditText>(R.id.producto_agregar_inputBuscarCodigo)


        val btnAtras = findViewById<ImageView>(R.id.btnAtras)

        val productoActivity = Intent(baseContext, ProductoActivity::class.java)


        btnAtras.setOnClickListener{
            runOnUiThread {
                productoActivity.putExtra("cod_negocio", cod_negocio)
                productoActivity.putExtra("direccion", direccion)
                productoActivity.putExtra("cod_categoria", cod_categoria)
                productoActivity.putExtra("cod_zona", cod_zona)
                productoActivity.putExtra("cod_canal", cod_canal)
                productoActivity.putExtra("ca_descripcion", ca_descripcion)
                startActivity(productoActivity)
            }
        }

        producto_agregarProducto.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val descripcion_producto = producto_agregar_inputBuscarCodigo.text.toString()
                val miNegocio = db.NegocioDao().get_codigo(cod_negocio)
                val numProductos = db.NegocioDao().getAll().size + 1

                var codigo_nuevo = cod_negocio + numProductos.toString()


                db.ProductoDao().insert(Producto(
                    0,
                    codigo_nuevo,
                    cod_categoria,
                    cod_negocio,
                    descripcion_producto,
                    "",
                    "",
                    "",
                    "",
                    "1",
                    ca_descripcion,
                    "",
                    miNegocio.distrito,
                    cod_zona,
                    cod_canal,
                    "1"
                ))
                runOnUiThread {
                    productoActivity.putExtra("cod_negocio", cod_negocio)
                    productoActivity.putExtra("direccion", direccion)
                    productoActivity.putExtra("cod_categoria", cod_categoria)
                    productoActivity.putExtra("cod_zona", cod_zona)
                    productoActivity.putExtra("cod_canal", cod_canal)
                    productoActivity.putExtra("ca_descripcion", ca_descripcion)
                    startActivity(productoActivity)
                }
            }


        }


    }
}