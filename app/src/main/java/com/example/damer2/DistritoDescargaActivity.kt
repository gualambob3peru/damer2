package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.adapter.DistritoDescargaAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.*
import com.example.damer2.services.contrato.ContratoInput
import com.example.damer2.services.contrato.ContratoResponse
import com.example.damer2.services.contrato.ContratoService
import com.example.damer2.services.distrito.DistritoEmailInput
import com.example.damer2.services.distrito.DistritoResponse
import com.example.damer2.services.distrito.DistritoService
import com.example.damer2.services.negocio.NegocioEmailDistritoInput
import com.example.damer2.services.negocio.NegocioResponse
import com.example.damer2.services.negocio.NegocioService
import com.example.damer2.shared.UsuarioApplication.Companion.prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DistritoDescargaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distrito_descarga)

        val db = AuditoriaDb(this)

        val email = prefs.getUsuario()["email"].toString()

        val distritoEmailInput = DistritoEmailInput(email)
        var service = DistritoService.create()
        var apiInterface = service.get_x_auditor(distritoEmailInput)
        val resumenActivity = Intent(baseContext, ResumenActivity::class.java)
        var btnAtras = findViewById<ImageView>(R.id.btnAtras)

        apiInterface.enqueue( object : Callback<DistritoResponse>
        {
            override fun onResponse(
                call: Call<DistritoResponse>,
                response: Response<DistritoResponse>
            ){
                var distritos = response.body()

                lifecycleScope.launch (Dispatchers.IO){
                    if (distritos != null) {
                        var arr_codigo : MutableList<String> = mutableListOf()
                        var arr_descripcion : MutableList<String> = mutableListOf()
                        var arr_distrito: MutableList<Distrito> = mutableListOf()

                        for(distrito in distritos.body){

                            if(db.DistritoUsuarioDao().get_x_codigo(distrito.codigo)>0){
                                continue
                            }
                            arr_codigo.add(distrito.codigo)
                            arr_descripcion.add(distrito.descripcion) // -->> Direccion
                            var n_distrito= Distrito(0,distrito.codigo,distrito.descripcion,distrito.cod_zona)
                            arr_distrito.add(n_distrito)
                        }

                        var adapter = DistritoDescargaAdapter()
                        adapter.setList(arr_codigo,arr_descripcion,arr_distrito)

                        adapter.onItemClick = { pro ->
                            val negocioEmailDistritoInput = NegocioEmailDistritoInput(email,pro.codigo)
                            var service = NegocioService.create()
                            var apiInterface2 = service.get_x_email_distrito(negocioEmailDistritoInput)

                            apiInterface2.enqueue( object : Callback<NegocioResponse> {
                                override fun onResponse(
                                    call: Call<NegocioResponse>,
                                    response: Response<NegocioResponse>
                                ){

                                    var negocios = response.body()

                                    if (negocios != null) {

                                        if(negocios.error.equals("0")){
                                            val contratoInput = ContratoInput(email)
                                            var service2 = ContratoService.create()
                                            var apiInterface3 = service2.getContratosConsolidado(contratoInput)
                                            apiInterface3.enqueue( object : Callback<ContratoResponse> {
                                                override fun onResponse(
                                                    call: Call<ContratoResponse>,
                                                    response2: Response<ContratoResponse>
                                                ) {
                                                    var contratos = response2.body()
                                                    if (contratos != null) {
                                                        if(contratos.error.equals("0")){
                                                            lifecycleScope.launch(Dispatchers.IO) {

                                                                //AGREGANDO CONTRATOS
                                                                db.ContratoDao().borrarTodo()
                                                                for (contrato in contratos.body){
                                                                    db.ContratoDao().insert(Contrato(
                                                                        0,
                                                                        contrato.cod_categoria,
                                                                        contrato.cod_zona,
                                                                        contrato.cod_canal,
                                                                        contrato.variables.joinToString(",")
                                                                    ))
                                                                }

                                                                //AGREGANDO NEGOCIOS con categorias y productos
                                                                for( negocio in negocios.body){

                                                                    if(db.DistritoUsuarioDao().get_x_codigo(negocio.cod_distrito)==0){
                                                                        db.DistritoUsuarioDao().insert(DistritoUsuario(
                                                                            0,
                                                                            negocio.cod_distrito,
                                                                            negocio.di_descripcion
                                                                        ))
                                                                    }


                                                                    db.NegocioDao().insert(Negocio(
                                                                        0,
                                                                        negocio.cod_negocio,
                                                                        negocio.direccion,
                                                                        "",
                                                                        "",
                                                                        negocio.cod_zona,
                                                                        negocio.cod_distrito,
                                                                        negocio.cod_canal,
                                                                        negocio.direccion,
                                                                        "1"

                                                                    ))

                                                                    var categorias = negocio.categorias

                                                                    for(cate in categorias){

                                                                        for(producto in cate.productos){
                                                                            db.ProductoDao().insert(
                                                                                Producto(
                                                                                    0,
                                                                                    producto.sku,
                                                                                    producto.cod_categoria,
                                                                                    negocio.cod_negocio,
                                                                                    producto.descripcion,
                                                                                    "",
                                                                                    "",
                                                                                    "",
                                                                                    "",
                                                                                    "",
                                                                                    cate.descripcion,
                                                                                    producto.vant,
                                                                                    "",
                                                                                    "",
                                                                                    "",
                                                                                    "0",
                                                                                    producto.compra_ant

                                                                                )
                                                                            )
                                                                        }

                                                                    }
                                                                }

                                                                runOnUiThread {
                                                                    resumenActivity.putExtra("medicion", prefs.getUsuario()["medicion"])
                                                                    startActivity(resumenActivity)
                                                                }

                                                            }
                                                        }
                                                    }
                                                }
                                                override fun onFailure( call: Call<ContratoResponse>, t: Throwable
                                                ) {
                                                    var ff = 2
                                                }
                                            })


                                        }else{
                                            //Hubo un error en el servidor
                                        }
                                    }else{
                                        //No hay negocios!
                                    }
                                }
                                override fun onFailure(call: Call<NegocioResponse>, t: Throwable){
                                    var ff=2
                                }
                            })
                        }

                        runOnUiThread {
                            val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(applicationContext)
                            var recyclerView = findViewById<RecyclerView>(R.id.distrito_lista)
                            recyclerView.layoutManager = linearLayoutManager
                            recyclerView.adapter = adapter
                        }


                    }
                }



            }
            override fun onFailure(call: Call<DistritoResponse>, t: Throwable){

            }
        })

        btnAtras.setOnClickListener{
            runOnUiThread {
                finish()
            }
        }

    }
}