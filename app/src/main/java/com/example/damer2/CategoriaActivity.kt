package com.example.damer2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import com.example.damer2.data.Entities.Producto
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriaActivity : AppCompatActivity() {

    var num = 1
    override fun onResume() {
        super.onResume()

        if(num!=1){
            actualizar();
        }else{
            num++
        }

    }
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
        var categoria_btnEditarNegocio = findViewById<ImageView>(R.id.categoria_btnEditarNegocio)

        var btnAtras = findViewById<ImageView>(R.id.btnAtras)

        val textView2 = findViewById<TextView>(R.id.txtMedicion)
        textView2.text = "MEDICION : "+ medicion.toString()


        val negocioActivity = Intent(baseContext, NegocioActivity::class.java)
        val categoriaNegocioEditarActivity = Intent(baseContext, CategoriaNegocioEditarActivity::class.java)





        var categoria_btnAgregar = findViewById<ImageView>(R.id.categoria_btnAgregar)

        val categoriaAgregarActivity = Intent(this, CategoriaAgregarActivity::class.java)


        val productoActivity = Intent(baseContext, ProductoActivity::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            var categorias = db.ProductoDao().getCategorias_negocio(cod_negocio)
            var miNegocio = db.NegocioDao().get_codigo(cod_negocio)

            runOnUiThread {
                categoria_txtNegocio.text = miNegocio.descripcion
            }

            var cod_zona = miNegocio.zona
            var cod_canal = miNegocio.canal

            if (categorias != null) {
                var arr_codigo: MutableList<String> = mutableListOf()
                var arr_descripcion: MutableList<String> = mutableListOf()
                var arr_categoria: MutableList<Categoria> = mutableListOf()
                var arr_excluir: MutableList<Int> = mutableListOf()

                for (categoria in categorias) {
                    val numExcluidosSinNegocio = db.ProductoDao().get_negocio_categoria_excluido(cod_negocio,categoria.codigo,2)
                    val numExcluidosConNegocio = db.ProductoDao().get_negocio_categoria_excluido(cod_negocio,categoria.codigo,3)

                    if(numExcluidosSinNegocio>0){
                        arr_excluir.add(2)
                    }else if(numExcluidosConNegocio>0){
                        arr_excluir.add(3)
                    }else{
                        arr_excluir.add(0)
                    }

                    arr_codigo.add(categoria.codigo)
                    arr_descripcion.add(categoria.descripcion) // -->> Direccion
                    var n_categoria = Categoria(0, categoria.codigo, categoria.descripcion)
                    arr_categoria.add(n_categoria)
                }

                var adapter = CategoriaAdapter()
                adapter.setList(arr_codigo, arr_descripcion, arr_categoria,arr_excluir)

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

                                                        //*************EXCLUIR A OTRO NEGOCIO*****

                                                        lifecycleScope.launch(Dispatchers.IO){
                                                            var negoElegido = arr_neg[which]

                                                            db.ProductoDao().update_estadoExcluido(cod_negocio,categoria.codigo,3)
                                                            val estadoExcluir = getEstadoNegocioExcluir(db,cod_negocio,categoria.codigo)
                                                            db.NegocioDao().update_estadoExcluido(cod_negocio,estadoExcluir);
                                                            //Revisar si esta volviendo al negocio inicial
                                                            //////Obteniendo un SKU
                                                            var produsInicial = db.ProductoDao().get_negocio_categoria(arr_cod_nego[which],categoria.codigo)
                                                            if(produsInicial.size!=0){ // Existen skus entonces se tiene que actualizar los skus finales
                                                                for(produ in produsInicial){
                                                                    db.ProductoDao().update_estadoExcluido(arr_cod_nego[which],categoria.codigo,0)
                                                                }
                                                            }else{//Como No existen se agregan al nuevo negocio
                                                                produsInicial = db.ProductoDao().get_negocio_categoria(cod_negocio,categoria.codigo)
                                                                for(produ in produsInicial){
                                                                    produ.id=0
                                                                    produ.cod_negocio = arr_cod_nego[which]
                                                                    produ.estadoCambiado = 1
                                                                    db.ProductoDao().insert(produ)
                                                                }
                                                            }

                                                            actualizar();

                                                        }
                                                    })

                                            val alert3 = builder3.create()
                                            alert3.show()
                                        }

                                    }

                                }
                                .setNegativeButton("NO") { dialog2, id2 ->
                                    lifecycleScope.launch(Dispatchers.IO){
                                        //Excluir pero sin enviarlo a otro negocio
                                        db.ProductoDao().update_estadoExcluido(cod_negocio,categoria.codigo,2)
                                        val estadoExcluir = getEstadoNegocioExcluir(db,cod_negocio,categoria.codigo)
                                        //actualizando el negocio con estadoExcluido=2 que indica que tiene negocios excluidos pendientes
                                        //db.NegocioDao().update_estadoExcluido(cod_negocio,2);
                                        db.NegocioDao().update_estadoExcluido(cod_negocio,estadoExcluir);

                                        dialog.dismiss()
                                        actualizar();
                                    }

                                }
                            val alert2 = builder2.create()
                            alert2.show()
                        }
                            //No hace nada
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

        categoria_btnEditarNegocio.setOnClickListener {
            runOnUiThread{
                categoriaNegocioEditarActivity.putExtra("cod_distrito", cod_distrito)
                categoriaNegocioEditarActivity.putExtra("di_descripcion", di_descripcion)
                categoriaNegocioEditarActivity.putExtra("cod_negocio", cod_negocio)
                categoriaNegocioEditarActivity.putExtra("direccion", direccion)
                startActivity(categoriaNegocioEditarActivity)
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

    fun getEstadoNegocioExcluir(db : AuditoriaDb,cod_negocio: String,cod_categoria: String):Int{
        val numExcluidosSinNegocio = db.ProductoDao().get_negocio_excluido(cod_negocio,2)
        val numExcluidosConNegocio = db.ProductoDao().get_negocio_excluido(cod_negocio,3)

        if(numExcluidosSinNegocio>0){
            return 2
        }else if(numExcluidosConNegocio>0){
            return 3
        }else{
            return 0
        }
    }

    fun actualizar (){

        val categoriaActivity = Intent(baseContext, CategoriaActivity::class.java)
        val email = UsuarioApplication.prefs.getUsuario()["email"].toString()
        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        var cod_negocio = intent.getStringExtra("cod_negocio").toString()
        var direccion = intent.getStringExtra("direccion").toString()
        var cod_distrito = intent.getStringExtra("cod_distrito").toString()
        var di_descripcion = intent.getStringExtra("di_descripcion").toString()

        categoriaActivity.putExtra("email", email)
        categoriaActivity.putExtra("direccion", direccion)
        categoriaActivity.putExtra("medicion", medicion)
        categoriaActivity.putExtra("cod_negocio", cod_negocio)
        categoriaActivity.putExtra("direccion", direccion)
        categoriaActivity.putExtra("cod_distrito", cod_distrito)
        categoriaActivity.putExtra("di_descripcion", di_descripcion)

        finish()
        startActivity(categoriaActivity)
    }

}