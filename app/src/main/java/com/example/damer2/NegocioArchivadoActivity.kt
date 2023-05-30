package com.example.damer2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.NegocioAdapter
import com.example.damer2.adapter.NegocioArchivadoAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NegocioArchivadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negocio_archivado)

        val db = AuditoriaDb(this)

        val email = UsuarioApplication.prefs.getUsuario()["email"].toString()
        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        var cod_distrito = intent.getStringExtra("cod_distrito").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()
        var di_descripcion = intent.getStringExtra("di_descripcion").toString()
        val recycleViewNegocioArchivado = findViewById<RecyclerView>(R.id.recycleViewNegocioArchivado)
        var btnAtras = findViewById<ImageView>(R.id.btnAtras)

        lifecycleScope.launch(Dispatchers.IO) {
            var negocios = db.NegocioDao().get_archivados(cod_distrito)

            if (negocios != null) {
                var arr_codigo: MutableList<String> = mutableListOf()
                var arr_descripcion: MutableList<String> = mutableListOf()
                var arr_negocio: MutableList<Negocio> = mutableListOf()
                var arr_num_vacios: MutableList<Int> = mutableListOf()
                var arr_estado_enviado: MutableList<Int> = mutableListOf()
                var arr_num_productos: MutableList<Int> = mutableListOf()
                val arr_canals: MutableList<String> = mutableListOf()

                for (negocio in negocios) {
                    arr_codigo.add(negocio.codigo_negocio)
                    arr_descripcion.add(negocio.descripcion.uppercase()) // -->> Direccion
                    val n_negocio = negocio
                    val numVacios = db.ProductoDao().getGuardados_by_negocio(negocio.codigo_negocio)
                    val estadoNegocio = db.NegocioDao().getEstadoEnviado(negocio.codigo_negocio)
                    val numProductos = db.ProductoDao().getAllProductos_negocio(negocio.codigo_negocio).size
                    val canal_desc = db.CanalDao().get_codigo(negocio.canal).descripcion

                    arr_negocio.add(n_negocio)
                    arr_num_vacios.add(numVacios)
                    arr_estado_enviado.add(estadoNegocio)
                    arr_num_productos.add(numProductos)
                    arr_canals.add(canal_desc)
                }

                var adapter = NegocioArchivadoAdapter()
                adapter.setList(arr_codigo, arr_descripcion, arr_negocio,arr_num_vacios,arr_estado_enviado,arr_num_productos,arr_canals)
                runOnUiThread {
                    val linearLayoutManager: LinearLayoutManager =
                        LinearLayoutManager(applicationContext)

                    recycleViewNegocioArchivado.layoutManager = linearLayoutManager
                    recycleViewNegocioArchivado.adapter = adapter


                }
            }
        }


        btnAtras.setOnClickListener{
            runOnUiThread {
                finish()
            }

        }
    }
}