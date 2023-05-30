package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.DistritoUsuarioAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.DistritoUsuario
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.data.Entities.Producto
import com.example.damer2.services.todo.MedicionResponse
import com.example.damer2.services.todo.TodoResponse
import com.example.damer2.services.todo.TodoService
import com.example.damer2.shared.UsuarioApplication.Companion.prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResumenActivity : AppCompatActivity() {
    var usuario = prefs.getUsuario()

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
        setContentView(R.layout.activity_resumen)

        val db = AuditoriaDb(this)
        var medicion = intent.getStringExtra("medicion")
        val negocio_descarga = Intent(baseContext, DistritoDescargaActivity::class.java)
        val negocioActivity = Intent(baseContext, NegocioActivity::class.java)
        val textView2 = findViewById<TextView>(R.id.textView2)
        val resumenCantidadEnviado = findViewById<TextView>(R.id.resumenCantidadEnviado)
        val resumenNegocioEnviado = findViewById<TextView>(R.id.resumenNegocioEnviado)
        val btnLimpiar = findViewById<ImageView>(R.id.btnLimpiar)

        textView2.text = "MEDICION : "+ medicion.toString()

        var resumen_btnDescargar = findViewById<Button>(R.id.resumen_btnDescargar)
        var resumen_tMensajeDistrito = findViewById<TextView>(R.id.resumen_tMensajeDistrito)

        var serviceTodo = TodoService.create()
        var apiInterfaceTodo = serviceTodo.getMedicion()

         apiInterfaceTodo.enqueue( object : Callback<MedicionResponse> {
            override fun onResponse(
                call: Call<MedicionResponse>,
                response: Response<MedicionResponse>
            ) {
                var usuario = response.body()
                if(usuario!=null){
                    var a=    usuario.body.medicion
                    if(a != prefs.getUsuario()["medicion"]){
                        val builder = AlertDialog.Builder(this@ResumenActivity)
                        builder.setMessage("La medicion no es actual, desea reiniciar datos?")
                            .setCancelable(false)
                            .setPositiveButton("SI"){dialog, id ->

                                lifecycleScope.launch(Dispatchers.IO){
                                    db.CanalDao().borrarTodo()
                                    db.CategoriaDao().borrarTodo()
                                    db.ContratoDao().borrarTodo()
                                    db.DistritoDao().borrarTodo()
                                    db.DistritoUsuarioDao().borrarTodo()
                                    db.GeneralDao().borrarTodo()
                                    db.GlobalDao().borrarTodo()
                                    db.NegocioDao().borrarTodo()
                                    db.ProductoDao().borrarAllAll()
                                    db.ProductoMasterDao().borrarAllAll()
                                    db.UsuarioDao().borrarTodo()
                                    db.ZonaDao().borrarTodo()
                                    finish()
                                }
                            }
                            .setNegativeButton("NO") { dialog, id ->

                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                    }
                }


            }

            override fun onFailure(call: Call<MedicionResponse>, t: Throwable) {
                var a=  2
            }
        });


        lifecycleScope.launch(Dispatchers.IO){
            //Hallando Resumenes
            val skuGuardados = db.ProductoDao().getAuditados()
            val skuTotal = db.ProductoDao().getTotal()
            val rSkus = "$skuGuardados/$skuTotal"
            resumenCantidadEnviado.text = rSkus

            val negociosEnviados = db.NegocioDao().getEnviados()
            val negociosTotal = db.NegocioDao().getTotal()
            val rNego = "$negociosEnviados/$negociosTotal"
            resumenNegocioEnviado.text = rNego

            var distritos = db.DistritoUsuarioDao().getAll()

            if(distritos.size!=0){
                var arr_codigo : MutableList<String> = mutableListOf()
                var arr_descripcion : MutableList<String> = mutableListOf()
                var arr_distrito : MutableList<DistritoUsuario> = mutableListOf()

                for(distrito in distritos){
                    arr_codigo.add(distrito.codigo)
                    arr_descripcion.add(distrito.descripcion) // -->> Direccion
                    var n_distrito= DistritoUsuario(0,distrito.codigo,distrito.descripcion,distrito.cod_zona)
                    arr_distrito.add(n_distrito)
                }

                var adapter = DistritoUsuarioAdapter()
                adapter.setList(arr_codigo,arr_descripcion,arr_distrito)

                adapter.onItemClick = { distrito ->
                    runOnUiThread {
                        negocioActivity.putExtra("cod_distrito",distrito.codigo)
                        negocioActivity.putExtra("di_descripcion",distrito.descripcion)
                        negocioActivity.putExtra("cod_zona",distrito.cod_zona)
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

        btnLimpiar.setOnClickListener {


                val builder = AlertDialog.Builder(this@ResumenActivity)
                builder.setMessage("¿Está seguro que desea borrar los datos?")
                    .setCancelable(false)
                    .setPositiveButton("SI"){dialog, id ->
                        lifecycleScope.launch(Dispatchers.IO){
                            db.CanalDao().borrarTodo()
                            db.CategoriaDao().borrarTodo()
                            db.ContratoDao().borrarTodo()
                            db.DistritoDao().borrarTodo()
                            db.DistritoUsuarioDao().borrarTodo()
                            db.GeneralDao().borrarTodo()
                            db.GlobalDao().borrarTodo()
                            db.NegocioDao().borrarTodo()
                            db.ProductoDao().borrarAllAll()
                            db.ProductoMasterDao().borrarAllAll()
                            db.UsuarioDao().borrarTodo()
                            db.ZonaDao().borrarTodo()
                            finish()

                            runOnUiThread {
                                val main = Intent(baseContext, MainActivity::class.java)

                                startActivity(main)
                            }
                        }
                    }
                    .setNegativeButton("NO") { dialog, id ->

                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()



        }
        resumen_btnDescargar.setOnClickListener {
            runOnUiThread {
                negocio_descarga.putExtra("medicion", prefs.getUsuario()["medicion"])
                startActivity(negocio_descarga)
            }

        }
    }

    fun actualizar (){
        val resumenActivity = Intent(baseContext, ResumenActivity::class.java)
        resumenActivity.putExtra("medicion", prefs.getUsuario()["medicion"])

        finish()
        startActivity(resumenActivity)


    }
}