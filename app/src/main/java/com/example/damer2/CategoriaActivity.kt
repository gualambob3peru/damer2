package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.CategoriaAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class CategoriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria)

        val db = AuditoriaDb(this)

        val email = UsuarioApplication.prefs.getUsuario()["email"].toString()
        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        var cod_negocio = intent.getStringExtra("cod_negocio").toString()
        var direccion = intent.getStringExtra("direccion").toString()
        var cod_distrito = intent.getStringExtra("cod_distrito").toString()
        var di_descripcion = intent.getStringExtra("di_descripcion").toString()

        var categoria_txtNegocio = findViewById<TextView>(R.id.categoria_txtNegocio)
        var btnAtras = findViewById<ImageView>(R.id.btnAtras)

        val negocioActivity = Intent(baseContext, NegocioActivity::class.java)

        runOnUiThread {
            categoria_txtNegocio.text = direccion
        }



        var categoria_btnAgregar = findViewById<TextView>(R.id.categoria_btnAgregar)

        val categoriaAgregarActivity = Intent(this, CategoriaAgregarActivity::class.java)


        val productoActivity = Intent(baseContext, ProductoActivity::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            var categorias = db.ProductoDao().getCategorias_negocio(cod_negocio)
            var miNegocio = db.NegocioDao().get_codigo(cod_negocio)
            var cod_zona = miNegocio.zona
            var cod_canal = miNegocio.canal

            if (categorias != null) {
                var arr_codigo: MutableList<String> = mutableListOf()
                var arr_descripcion: MutableList<String> = mutableListOf()
                var arr_categoria: MutableList<Categoria> = mutableListOf()

                for (categoria in categorias) {

                    arr_codigo.add(categoria.codigo)
                    arr_descripcion.add(categoria.descripcion) // -->> Direccion
                    var n_categoria = Categoria(0, categoria.codigo, categoria.descripcion)
                    arr_categoria.add(n_categoria)
                }

                var adapter = CategoriaAdapter()
                adapter.setList(arr_codigo, arr_descripcion, arr_categoria)

                adapter.onItemClick = { categoria ->
                    runOnUiThread {
                        productoActivity.putExtra("cod_negocio", cod_negocio)
                        productoActivity.putExtra("direccion", direccion)
                        productoActivity.putExtra("cod_categoria", categoria.codigo)
                        productoActivity.putExtra("cod_zona", cod_zona)
                        productoActivity.putExtra("cod_canal", cod_canal)
                        productoActivity.putExtra("ca_descripcion", categoria.descripcion)
                        startActivity(productoActivity)
                    }

                }


                val linearLayoutManager: LinearLayoutManager =
                    LinearLayoutManager(applicationContext)
                var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategoria)
                recyclerView.layoutManager = linearLayoutManager
                recyclerView.adapter = adapter

            } else {
                //No tiene ninguna categoria
            }


        }

        categoria_btnAgregar.setOnClickListener {
            runOnUiThread{

                categoriaAgregarActivity.putExtra("cod_negocio", cod_negocio)
                categoriaAgregarActivity.putExtra("direccion", direccion)
                startActivity(categoriaAgregarActivity)
            }
        }

        btnAtras.setOnClickListener{
            runOnUiThread {
                negocioActivity.putExtra("cod_distrito", cod_distrito)
                negocioActivity.putExtra("di_descripcion", di_descripcion)
                startActivity(negocioActivity)
            }

        }

    }

}