package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.DistritoUsuarioAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.DistritoUsuario
import com.example.damer2.shared.UsuarioApplication.Companion.prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResumenActivity : AppCompatActivity() {
    var usuario = prefs.getUsuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        val db = AuditoriaDb(this)
        var medicion = intent.getStringExtra("medicion")
        val negocio_descarga = Intent(baseContext, DistritoDescargaActivity::class.java)
        val negocioActivity = Intent(baseContext, NegocioActivity::class.java)

        var resumen_btnDescargar = findViewById<Button>(R.id.resumen_btnDescargar)
        var resumen_tMensajeDistrito = findViewById<TextView>(R.id.resumen_tMensajeDistrito)

        lifecycleScope.launch(Dispatchers.IO){
            var distritos = db.DistritoUsuarioDao().getAll()

            if(distritos.size!=0){

                var arr_codigo : MutableList<String> = mutableListOf()
                var arr_descripcion : MutableList<String> = mutableListOf()
                var arr_distrito : MutableList<DistritoUsuario> = mutableListOf()

                for(distrito in distritos){
                    arr_codigo.add(distrito.codigo)
                    arr_descripcion.add(distrito.descripcion) // -->> Direccion
                    var n_distrito= DistritoUsuario(0,distrito.codigo,distrito.descripcion)
                    arr_distrito.add(n_distrito)
                }

                var adapter = DistritoUsuarioAdapter()
                adapter.setList(arr_codigo,arr_descripcion,arr_distrito)

                adapter.onItemClick = { distrito ->

                    runOnUiThread {
                        negocioActivity.putExtra("cod_distrito",distrito.codigo)
                        negocioActivity.putExtra("di_descripcion",distrito.descripcion)
                        startActivity(negocioActivity)
                    }

                }
                runOnUiThread {
                    val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
                    var recyclerView = findViewById<RecyclerView>(R.id.recycleViewDistritoUsuario)
                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapter
                }

            }else{
                runOnUiThread {
                    resumen_tMensajeDistrito.text = "No hay distritos descargados..."
                }


            }


        }


        resumen_btnDescargar.setOnClickListener {
            runOnUiThread {
                negocio_descarga.putExtra("medicion", prefs.getUsuario()["medicion"])
                startActivity(negocio_descarga)
            }

        }
    }
}