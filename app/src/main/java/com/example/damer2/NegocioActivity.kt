package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.DistritoUsuarioAdapter
import com.example.damer2.adapter.NegocioAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.DistritoUsuario
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NegocioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negocio)

        val db = AuditoriaDb(this)

        val email = UsuarioApplication.prefs.getUsuario()["email"].toString()
        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        var cod_distrito = intent.getStringExtra("cod_distrito").toString()
        var di_descripcion = intent.getStringExtra("di_descripcion").toString()
        val categoriaActivity = Intent(baseContext, CategoriaActivity::class.java)

        var btnAtras = findViewById<ImageView>(R.id.btnAtras)

        var negocio_txtDistrito = findViewById<TextView>(R.id.negocio_txtDistrito)
        negocio_txtDistrito.text = di_descripcion

        lifecycleScope.launch(Dispatchers.IO) {
            var negocios = db.NegocioDao().get_distrito(cod_distrito)

            if (negocios != null) {
                var arr_codigo: MutableList<String> = mutableListOf()
                var arr_descripcion: MutableList<String> = mutableListOf()
                var arr_negocio: MutableList<Negocio> = mutableListOf()

                for (negocio in negocios) {
                    arr_codigo.add(negocio.codigo_negocio)
                    arr_descripcion.add(negocio.descripcion) // -->> Direccion
                    var n_negocio = Negocio(0, negocio.codigo_negocio, negocio.descripcion)
                    arr_negocio.add(n_negocio)
                }

                var adapter = NegocioAdapter()
                adapter.setList(arr_codigo, arr_descripcion, arr_negocio)

                adapter.onItemClick = { negocio ->

                    runOnUiThread {
                        categoriaActivity.putExtra("cod_distrito", cod_distrito)
                        categoriaActivity.putExtra("di_descripcion", di_descripcion)
                        categoriaActivity.putExtra("cod_negocio", negocio.codigo_negocio)
                        categoriaActivity.putExtra("direccion", negocio.descripcion)
                        startActivity(categoriaActivity)
                    }

                    /*negocio_categoria_activity.putExtra("cod_negocio",codigo_negocio.text)
                    negocio_categoria_activity.putExtra("descripcion_negocio",descripcion_negocio.text)
                    startActivity(negocio_categoria_activity)*/
                }

                val linearLayoutManager: LinearLayoutManager =
                    LinearLayoutManager(applicationContext)
                var recyclerView = findViewById<RecyclerView>(R.id.recycleViewNegocio)
                recyclerView.layoutManager = linearLayoutManager
                recyclerView.adapter = adapter
            } else {
                //No hay distritos
            }
        }

        btnAtras.setOnClickListener{
            runOnUiThread {

                val resumenActivity = Intent(baseContext, ResumenActivity::class.java)
                resumenActivity.putExtra("medicion", UsuarioApplication.prefs.getUsuario()["medicion"])
                startActivity(resumenActivity)
            }

        }
    }
}