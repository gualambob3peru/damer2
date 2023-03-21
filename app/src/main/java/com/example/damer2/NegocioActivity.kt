package com.example.damer2

import UploadStreamRequestBody
import android.R.attr.bitmap
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.damer2.services.todo.MedicionResponse
import com.example.damer2.services.todo.TodoService
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
        setContentView(R.layout.activity_negocio)

        val db = AuditoriaDb(this)

        val email = UsuarioApplication.prefs.getUsuario()["email"].toString()
        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        var cod_distrito = intent.getStringExtra("cod_distrito").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()
        var di_descripcion = intent.getStringExtra("di_descripcion").toString()
        val categoriaActivity = Intent(baseContext, CategoriaActivity::class.java)
        val producto_tMensaje = findViewById<TextView>(R.id.producto_tMensaje)
        val negocio_btnAgregar = findViewById<ImageView>(R.id.negocio_btnAgregar)
        val negocio_btnBuscar = findViewById<Button>(R.id.negocio_btnBuscar)
        val negocio_txtBuscarNegocio = findViewById<TextView>(R.id.negocio_txtBuscarNegocio)


        val textView2 = findViewById<TextView>(R.id.txtMedicion)
        textView2.text = "MEDICION : "+ medicion.toString()
        val recyclerView = findViewById<RecyclerView>(R.id.recycleViewNegocio)

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

                var adapter = NegocioAdapter()
                adapter.setList(arr_codigo, arr_descripcion, arr_negocio,arr_num_vacios,arr_estado_enviado,arr_num_productos,arr_canals)

                adapter.onItemButtonClick = {negocio,boton ->
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
                                    val builder = AlertDialog.Builder(this@NegocioActivity)
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
                                }else{
                                    lifecycleScope.launch(Dispatchers.IO){
                                        val numVacios = db.ProductoDao().getGuardados_by_negocio(negocio.codigo_negocio)
                                        val estadoNegocio = db.NegocioDao().getEstadoEnviado(negocio.codigo_negocio)
                                        val numProductos = db.ProductoDao().getAllProductos_negocio(negocio.codigo_negocio).size

                                        if(negocio.estadoTemporal==2){
                                            lifecycleScope.launch(Dispatchers.IO) {
                                                var productos = db.ProductoDao().getAllProductos_negocio(negocio.codigo_negocio)
                                                var elNegocio = db.NegocioDao().get_codigo(negocio.codigo_negocio)
                                                subirProductos(productos,elNegocio)

                                            }
                                        }else{
                                            if(numProductos!=0){
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
                                            }else{
                                                runOnUiThread {
                                                    producto_tMensaje.text = "No hay productos para enviar"
                                                }
                                            }
                                        }




                                    }

                                }
                            }


                        }

                        override fun onFailure(call: Call<MedicionResponse>, t: Throwable) {
                            var a=  2
                        }
                    });





                }

                adapter.onItemClick = { negocio ->
                    lifecycleScope.launch(Dispatchers.IO){
                        val estadoNegocio = db.NegocioDao().getEstadoEnviado(negocio.codigo_negocio)

                        if(estadoNegocio!=1 && negocio.estadoTemporal!=2){//Si no esta enviado se puede ingresar a categorias
                            runOnUiThread {
                                categoriaActivity.putExtra("cod_distrito", cod_distrito)
                                categoriaActivity.putExtra("di_descripcion", di_descripcion)
                                categoriaActivity.putExtra("cod_negocio", negocio.codigo_negocio)
                                categoriaActivity.putExtra("direccion", negocio.descripcion)
                                startActivity(categoriaActivity)
                            }
                        }
                    }



                    /*negocio_categoria_activity.putExtra("cod_negocio",codigo_negocio.text)
                    negocio_categoria_activity.putExtra("descripcion_negocio",descripcion_negocio.text)
                    startActivity(negocio_categoria_activity)*/
                }

                adapter.onItemBtnTemporalClick = { negocio,boton ->

                        if(negocio.estadoTemporal==0){
                            val builder = AlertDialog.Builder(this@NegocioActivity)
                            builder.setMessage("Desea la baja definitiva?")
                                .setCancelable(false)
                                .setPositiveButton("SI"){dialog, id ->
                                    val cod_generado = (System.currentTimeMillis().toString().substring(5).toInt() + 1).toString()

                                    lifecycleScope.launch(Dispatchers.IO){
                                        db.NegocioDao().update_estadoTemporal(negocio.codigo_negocio,2) //BAJA DEFINITIVA
                                        db.ProductoDao().update_estadoTemporal(negocio.codigo_negocio,2) //BAJA DEFINITIVA
                                        //AGREGANDO EL NEGOCIO TEMPORAL
                                        var miNegocio = Negocio(
                                            0,
                                            cod_generado,
                                            "A   DIRECCION TEMPORAL",
                                            "",
                                            "",
                                            cod_zona,
                                            cod_distrito,
                                            negocio.canal,
                                            "     NEGOCIO TEMPORAL",
                                            "1",
                                            "1",
                                            "0",
                                            "",
                                            0,
                                            0,
                                            1

                                        )
                                        db.NegocioDao().insert(miNegocio)

                                        //CATEGORIAS
                                        var categorias = db.ProductoDao().getCategorias_negocio(negocio.codigo_negocio)
                                        for(cate in categorias){
                                            db.ProductoDao().insert(Producto(0,
                                                "",
                                                cate.codigo,
                                                cod_generado,
                                                "",
                                                "",
                                                "",
                                                "",
                                                "",
                                                "1",
                                                cate.descripcion)
                                            )
                                        }


                                        actualizar()
                                    }


                                }
                                .setNegativeButton("NO") { dialog, id ->

                                    dialog.dismiss()
                                }
                            val alert = builder.create()
                            alert.show()
                        }




                }

                runOnUiThread {
                    val linearLayoutManager: LinearLayoutManager =
                        LinearLayoutManager(applicationContext)

                    recyclerView.layoutManager = linearLayoutManager
                    recyclerView.adapter = adapter


                }
                //mAlertDialog.cancel()
            } else {
                //No hay distritos
            }
        }

        btnAtras.setOnClickListener{
            runOnUiThread {
               finish()
            }

        }

        negocio_btnBuscar.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){

                val texto = negocio_txtBuscarNegocio.text.toString()
                var negocios = db.NegocioDao().get_distrito(cod_distrito)

                for(i in negocios.indices){
                    if(negocios[i].descripcion.uppercase().contains(texto.uppercase()) || negocios[i].codigo_negocio.uppercase().contains(texto.uppercase())){
                        runOnUiThread {

                            recyclerView.scrollToPosition(i)

                        }
                        break
                    }
                }


            }
        }

        negocio_btnAgregar.setOnClickListener {
            val negocioAgregarActivity = Intent(baseContext, NegocioAgregarActivity::class.java)
            negocioAgregarActivity.putExtra("medicion", UsuarioApplication.prefs.getUsuario()["medicion"])
            negocioAgregarActivity.putExtra("cod_distrito", cod_distrito)
            negocioAgregarActivity.putExtra("cod_zona", cod_zona)
            startActivity(negocioAgregarActivity)
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
                    //Actualizando estado de negocios y productos enviados
                    db.NegocioDao().update_estadoenviado(miNegocio.codigo_negocio,1)
                    for(produ in productos){
                        db.ProductoDao().update_estadoEnviado(1)
                    }

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
                        actualizar()
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
        lifecycleScope.launch(Dispatchers.IO) {
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
                        t.toString()
                       // TODO(t.toString() + "fff")
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


    fun actualizar (){
        val negocioActivity = Intent(baseContext, NegocioActivity::class.java)
        var cod_distrito = intent.getStringExtra("cod_distrito").toString()
        var di_descripcion = intent.getStringExtra("di_descripcion").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()
        negocioActivity.putExtra("cod_distrito",cod_distrito)
        negocioActivity.putExtra("cod_zona",cod_zona)
        negocioActivity.putExtra("di_descripcion",di_descripcion)

        finish()
        startActivity(negocioActivity)


    }
}