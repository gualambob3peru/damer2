package com.example.damer2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.CategoriaAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                        productoActivity.putExtra("cod_distrito", cod_distrito)
                        productoActivity.putExtra("di_descripcion", di_descripcion)
                        productoActivity.putExtra("direccion", direccion)
                        productoActivity.putExtra("cod_categoria", categoria.codigo)
                        productoActivity.putExtra("cod_zona", cod_zona)
                        productoActivity.putExtra("cod_canal", cod_canal)
                        productoActivity.putExtra("ca_descripcion", categoria.descripcion)
                        startActivity(productoActivity)
                    }

                }

                adapter.onItemExcluirClick = {categoria ->
                    val builder = AlertDialog.Builder(this@CategoriaActivity)
                    builder.setMessage("Desea excluir?")
                        .setCancelable(false)
                        .setPositiveButton("SI") { dialog, id ->
                            val builder2 = AlertDialog.Builder(this@CategoriaActivity)
                            builder2.setMessage("Desea incluirlo en otro negocio?")
                                .setCancelable(false)
                                .setPositiveButton("SI") { dialog2, id2 ->

                                    lifecycleScope.launch(Dispatchers.IO) {
                                        //Negocios disponibles
                                        var negociosDisponibles = db.NegocioDao().getNegociosIncluir(
                                            miNegocio.canal,
                                            miNegocio.distrito
                                        )

                                        val arr_neg = mutableListOf<CharSequence>()
                                        val arr_cod_nego = mutableListOf<String>()

                                        //Quitando los negocios que ya tienen esa categoria
                                        for(ne in negociosDisponibles){
                                            //no agregando a los negocios que ya tienen esa categoria
                                            val tieneCateg = db.ProductoDao().getProducto_count_menosCategoria(ne.codigo_negocio,categoria.codigo)

                                            if(tieneCateg==0){
                                                arr_neg.add(ne.descripcion)
                                                arr_cod_nego.add(ne.codigo_negocio)
                                            }
                                        }




                                        val items = arr_neg.toTypedArray()

                                        runOnUiThread{
                                            val builder3 = AlertDialog.Builder(this@CategoriaActivity)
                                            builder3
                                                .setTitle("Negocios")
                                                .setItems(items,
                                                    DialogInterface.OnClickListener { dialog3, which ->

                                                        lifecycleScope.launch(Dispatchers.IO){
                                                            var negoEle = arr_neg[which]

                                                            db.ProductoDao().update_estadoExcluido(cod_negocio,categoria.codigo,3)
                                                            db.ProductoDao().update_negocio(cod_negocio,categoria.codigo,arr_cod_nego[which])
                                                        }
                                                    })

                                            val alert3 = builder3.create()
                                            alert3.show()
                                        }

                                    }

                                }
                                .setNegativeButton("NO") { dialog2, id2 ->

                                    dialog.dismiss()
                                }
                            val alert2 = builder2.create()
                            alert2.show()
                        }
                        .setNegativeButton("NO") { dialog, id ->

                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }

                runOnUiThread{
                    val linearLayoutManager: LinearLayoutManager =
                        LinearLayoutManager(applicationContext)
                    var recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategoria)
                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapter
                }


            } else {
                //No tiene ninguna categoria
            }


        }

        categoria_btnAgregar.setOnClickListener {
            runOnUiThread{
                categoriaAgregarActivity.putExtra("cod_distrito", cod_distrito)
                categoriaAgregarActivity.putExtra("di_descripcion", di_descripcion)
                categoriaAgregarActivity.putExtra("cod_negocio", cod_negocio)
                categoriaAgregarActivity.putExtra("direccion", direccion)
                startActivity(categoriaAgregarActivity)
            }
        }

        btnAtras.setOnClickListener{
            finish()
          /*  runOnUiThread {
                negocioActivity.putExtra("cod_distrito", cod_distrito)
                negocioActivity.putExtra("di_descripcion", di_descripcion)
                startActivity(negocioActivity)
            }*/

        }

    }

}