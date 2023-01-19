package com.example.damer2

import UploadStreamRequestBody
import android.R.attr.bitmap
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.damer2.adapter.NegocioAdapter
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.data.Entities.Producto
import com.example.damer2.services.imagenService.ImageApiResponse
import com.example.damer2.services.imagenService.ImageApiService
import com.example.damer2.services.medicion.ProductoService
import com.example.damer2.services.medicion.ProductoSubirInput
import com.example.damer2.services.medicion.ProductoSubirResponse
import com.example.damer2.shared.UsuarioApplication
import com.example.damer2.shared.UsuarioApplication.Companion.prefs
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        val producto_tMensaje = findViewById<TextView>(R.id.producto_tMensaje)

        var btnAtras = findViewById<ImageView>(R.id.btnAtras)

        var negocio_txtDistrito = findViewById<TextView>(R.id.negocio_txtDistrito)
        negocio_txtDistrito.text = di_descripcion

        lifecycleScope.launch(Dispatchers.IO) {
            var negocios = db.NegocioDao().get_distrito(cod_distrito)

            if (negocios != null) {
                var arr_codigo: MutableList<String> = mutableListOf()
                var arr_descripcion: MutableList<String> = mutableListOf()
                var arr_negocio: MutableList<Negocio> = mutableListOf()
                var arr_num_vacios: MutableList<Int> = mutableListOf()
                var arr_estado_enviado: MutableList<Int> = mutableListOf()

                for (negocio in negocios) {
                    arr_codigo.add(negocio.codigo_negocio)
                    arr_descripcion.add(negocio.descripcion) // -->> Direccion
                    val n_negocio = Negocio(0, negocio.codigo_negocio, negocio.descripcion)
                    val numVacios = db.ProductoDao().getNumCampoVacio_by_negocio(negocio.codigo_negocio)
                    val estadoNegocio = db.NegocioDao().getEstadoEnviado(negocio.codigo_negocio)

                    arr_negocio.add(n_negocio)
                    arr_num_vacios.add(numVacios)
                    arr_estado_enviado.add(estadoNegocio)
                }

                var adapter = NegocioAdapter()
                adapter.setList(arr_codigo, arr_descripcion, arr_negocio,arr_num_vacios,arr_estado_enviado)

                adapter.onItemButtonClick = {negocio,boton ->
                    lifecycleScope.launch(Dispatchers.IO){
                        val numVacios = db.ProductoDao().getNumCampoVacio_by_negocio(negocio.codigo_negocio)
                        val estadoNegocio = db.NegocioDao().getEstadoEnviado(negocio.codigo_negocio)

                        if(estadoNegocio==1){//verde
                            runOnUiThread {
                                producto_tMensaje.text = "Ya fue enviado"
                            }
                        }else{
                            if(numVacios>0){//Rojo
                                runOnUiThread {
                                    producto_tMensaje.text = "Falta completar SKUS"
                                }

                            }else if(numVacios==0){//Amarillo
                                lifecycleScope.launch(Dispatchers.IO) {
                                    var productos = db.ProductoDao().getAllProductos_negocio(negocio.codigo_negocio)
                                    var elNegocio = db.NegocioDao().get_codigo(negocio.codigo_negocio)
                                    subirProductos(productos,elNegocio)

                                }
                            }
                        }


                    }




                }

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

                runOnUiThread {
                    val linearLayoutManager: LinearLayoutManager =
                        LinearLayoutManager(applicationContext)
                    var recyclerView = findViewById<RecyclerView>(R.id.recycleViewNegocio)
                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapter
                }

            } else {
                //No hay distritos
            }
        }

        btnAtras.setOnClickListener{
            runOnUiThread {
               finish()
              /*  val resumenActivity = Intent(baseContext, ResumenActivity::class.java)
                resumenActivity.putExtra("medicion", UsuarioApplication.prefs.getUsuario()["medicion"])
                startActivity(resumenActivity)*/
            }

        }
    }

    private fun subirProductos(productos: List<Producto>, miNegocio: Negocio) {
        var datosSubida = mutableMapOf<String,String>()
        datosSubida["email"] = prefs.getUsuario()["email"].toString()
        datosSubida["medicion"] = prefs.getUsuario()["medicion"].toString()
        datosSubida["anio"] = prefs.getUsuario()["anio"].toString()
        datosSubida["mes"] = prefs.getUsuario()["mes"].toString()
        datosSubida["cod_negocio"] = miNegocio.codigo_negocio
        datosSubida["direccion_negocio"] = miNegocio.descripcion
        datosSubida["nombre_negocio"] = miNegocio.nombre
        datosSubida["telefono_negocio"] = miNegocio.telefono
        datosSubida["vendedor_negocio"] = miNegocio.vendedor

        datosSubida["cod_zona"] = miNegocio.zona
        datosSubida["cod_canal"] = miNegocio.canal
        datosSubida["cod_distrito"] = miNegocio.distrito
        val db = AuditoriaDb(this)
        val producto_tMensaje = findViewById<TextView>(R.id.producto_tMensaje)

        val productoSubirInput = ProductoSubirInput(productos,datosSubida)

        runOnUiThread {
            producto_tMensaje.text = "Subiendo Negocio..."
        }
        var service = ProductoService.create()
        var apiInterface = service.subir(productoSubirInput)
        apiInterface.enqueue( object : Callback<ProductoSubirResponse>
        {
            override fun onResponse(
                call: Call<ProductoSubirResponse>,
                response: Response<ProductoSubirResponse>
            ) {
                var todo = response.body()

                lifecycleScope.launch(Dispatchers.IO) {
                    db.NegocioDao().update_estadoenviado(miNegocio.codigo_negocio,1)
                   // uploadFile()
                    val directoryImage = Environment.DIRECTORY_PICTURES
                    val directory = getExternalFilesDir(directoryImage)!!
                    var count = 0;

                    directory.walk().forEach {
                        if(it.isFile ){
                            if(it.parentFile.parentFile.name == miNegocio.codigo_negocio){
                                count++;
                                var cod_sku_img = it.parentFile.name
                                var cod_negocio_img = it.parentFile.parentFile.name

                                uploadFile(it.toUri())
                            }
                        }
                    }

                    if(count>0){
                        runOnUiThread {
                            producto_tMensaje.text = "Subiendo Fotos..."
                        }
                    }else{
                        runOnUiThread {
                            producto_tMensaje.text = "Negocio subido"
                        }
                    }

                }
            }
            override fun onFailure(call: Call<ProductoSubirResponse>, t: Throwable) {
                runOnUiThread {
                    producto_tMensaje.text = "Revise su conexión"
                }

                //btnBuscarMedicion.isEnabled= true
                //TODO(t.toString() + "fff")
            }

        })
    }

    private fun uploadFile(uri: Uri) {
        lifecycleScope.launch {
            val producto_tMensaje = findViewById<TextView>(R.id.producto_tMensaje)

            val stream = contentResolver.openInputStream(uri) ?: return@launch
            val request = UploadStreamRequestBody("image/*", stream, onUploadProgress = {
                Log.d("MyActivity", "On upload progress $it")
                //viewBinding.progressView.progress = it // Some ProgressBar
            })
            val filePart = MultipartBody.Part.createFormData(
                "file",
                "1.jpg",
                request
            )
            val myImage = uri.toFile()
            val cod_sku_img = myImage.parentFile.name.toInt()
            val cod_negocio_img = myImage.parentFile.parentFile.name.toInt()

            try {
                var service = ImageApiService.create()
                var apiInterface = service.uploadFile(filePart,cod_sku_img,cod_negocio_img )

                apiInterface.enqueue( object : Callback<ImageApiResponse>
                {
                    override fun onResponse(
                        call: Call<ImageApiResponse>,
                        response: Response<ImageApiResponse>
                    ) {
                        var todo = response.body()

                        if(todo!=null){
                            var res = todo.body
                            if(res=="1"){
                                runOnUiThread {
                                    producto_tMensaje.text = "Negocio Subido"
                                }
                            }else{
                                runOnUiThread {
                                    producto_tMensaje.text = "Error en el servidor"
                                }
                            }
                        }


                    }
                    override fun onFailure(call: Call<ImageApiResponse>, t: Throwable) {
                        runOnUiThread {
                            producto_tMensaje.text = "Error en el servidor"
                        }
                        TODO(t.toString() + "fff")
                    }

                })
            }
            catch(e: Exception) { // if something happens to the network
                var a =e
              //  Snackbar.make(viewBinding.root, "Something went wrong", Snackbar.LENGTH_SHORT).show()
                return@launch
            }
            Log.d("MyActivity", "On finish upload file")
        }
    }
}