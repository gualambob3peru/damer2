package com.example.damer2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Dao.GeneralDao
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.*
import com.example.damer2.global.GlobalVar.Companion.RUTA_API
import com.example.damer2.services.LoginInput
import com.example.damer2.services.LoginResponse
import com.example.damer2.services.LoginService
import com.example.damer2.services.todo.TodoResponse
import com.example.damer2.services.todo.TodoService
import com.example.damer2.shared.UsuarioApplication.Companion.prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val db = AuditoriaDb(this)

        val botonLogin = findViewById<ImageView>(R.id.btnLogin)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtPassword = findViewById<TextView>(R.id.txtPassword)
        val tmensaje = findViewById<TextView>(R.id.tmensaje)
        val resumenActivity = Intent(baseContext, ResumenActivity::class.java)


        lifecycleScope.launch(Dispatchers.IO){
            var miGlobal = db.GlobalDao().get_codigo("ruta_api")
            if(miGlobal==null){
                prefs.setRutaApi(RUTA_API)
            }
        }



        botonLogin.setOnClickListener {

            if(txtEmail.text.toString().equals("") || txtPassword.text.toString().equals("") ){
                runOnUiThread {

                    tmensaje.text = "Debe rellenar todos los campos"
                }
            }else{
                runOnUiThread {

                    tmensaje.text = "Descargando datos..."
                    botonLogin.isEnabled= false
                }

                val loginInput = LoginInput(txtEmail.text.toString(), txtPassword.text.toString())
                var service = LoginService.create()
                var apiInterface = service.validar(loginInput)

                apiInterface.enqueue( object : Callback<LoginResponse>
                {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        var usuario = response.body()

                        if (usuario != null) {
                            if(usuario.error.equals("0")){
                                lifecycleScope.launch(Dispatchers.IO){
                                    var Produ = db.ProductoMasterDao().getCount()

                                    if(Produ!=0) {
                                        if(prefs.getUsuario()["email"]?.uppercase() == txtEmail.text.toString().uppercase()){
                                            runOnUiThread {

                                                botonLogin.isEnabled = true
                                                val resumenActivity =
                                                    Intent(baseContext, ResumenActivity::class.java)
                                                resumenActivity.putExtra(
                                                    "medicion",
                                                    prefs.getUsuario()["medicion"]
                                                )
                                                startActivity(resumenActivity)

                                            }
                                            return@launch
                                        }


                                    }

                                    db.GeneralDao().borrarTodo() //Borrando parametros de medicion


                                    var serviceTodo = TodoService.create()
                                    var apiInterfaceTodo = serviceTodo.get()

                                    apiInterfaceTodo.enqueue( object : Callback<TodoResponse>
                                    {
                                        override fun onResponse(
                                            call: Call<TodoResponse>,
                                            response: Response<TodoResponse>
                                        ) {
                                            lifecycleScope.launch(Dispatchers.IO){
                                                var todo = response.body()

                                                runOnUiThread {

                                                    tmensaje.text = "Cargando datos..."
                                                }
                                                if (todo != null) {
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


                                                    var categorias = todo.body.categorias
                                                    if(categorias!=null){
                                                        db.CategoriaDao().borrarTodo()
                                                        for(categoria in categorias){
                                                            db.CategoriaDao().insert(Categoria(0,categoria.codigo,categoria.descripcion,"1",categoria.tipoDato))

                                                        }
                                                    }

                                                    var zonas = todo.body.zonas
                                                    if(zonas!=null){
                                                        db.ZonaDao().borrarTodo()
                                                        for(zona in zonas){
                                                            db.ZonaDao().insert(Zona(0,zona.codigo,zona.descripcion))

                                                        }
                                                    }

                                                    var canals = todo.body.canals
                                                    if(canals!=null){
                                                        db.CanalDao().borrarTodo()
                                                        for(canal in canals){
                                                            db.CanalDao().insert(Canal(0,canal.codigo,canal.descripcion))

                                                        }
                                                    }

                                                    var distritos = todo.body.distritos
                                                    if(distritos!=null){
                                                        db.DistritoDao().borrarTodo()
                                                        for(distrito in distritos){
                                                            db.DistritoDao().insert(Distrito(0,distrito.codigo,distrito.descripcion,distrito.cod_zona))

                                                        }
                                                    }

                                                    var productos = todo.body.productos
                                                    if(productos!=null){
                                                        db.ProductoMasterDao().borrarAllAll()
                                                        for(producto in productos){
                                                            db.ProductoMasterDao().insert(
                                                                ProductoMaster(
                                                                    0,
                                                                    producto.sku,
                                                                    producto.descripcion,
                                                                    "1",
                                                                    producto.cod_categoria
                                                                )
                                                            )
                                                        }
                                                    }


                                                    db.UsuarioDao().borrarTodo()

                                                    val u = usuario.user[0]

                                                    var miUsuario = Usuario(
                                                        u.id.toInt(),
                                                        u.nombres,
                                                        u.apellidoPaterno,
                                                        u.apellidoMaterno,
                                                        txtPassword.text.toString(),
                                                        u.email,
                                                        u.telefono,
                                                        u.created_at,
                                                        u.idCargo,
                                                        u.estado,
                                                        u.idTipoDocumento,
                                                        u.nroDocumento
                                                    )

                                                    db.UsuarioDao().insert(listOf(miUsuario))




                                                    //Guardando en shared usuario
                                                    var sharedUsuario = emptyMap<String,String>().toMutableMap()
                                                    sharedUsuario["id"] = u.id.toString()
                                                    sharedUsuario["nombres"] = u.nombres
                                                    sharedUsuario["apellidoPaterno"] = u.apellidoPaterno
                                                    sharedUsuario["apellidoMaterno"] = u.apellidoMaterno
                                                    sharedUsuario["password"] = u.password
                                                    sharedUsuario["email"] = u.email
                                                    sharedUsuario["telefono"] = u.telefono
                                                    sharedUsuario["created_at"] = u.created_at
                                                    sharedUsuario["idCargo"] = u.idCargo
                                                    sharedUsuario["estado"] = u.estado
                                                    sharedUsuario["idTipoDocumento"] = u.idTipoDocumento
                                                    sharedUsuario["nroDocumento"] = u.nroDocumento
                                                    sharedUsuario["medicion"] = todo.body.datos["medicion"].toString()
                                                    sharedUsuario["anio"] = todo.body.datos["anio"].toString()
                                                    sharedUsuario["mes"] = todo.body.datos["mes"].toString()
                                                    sharedUsuario["contrasena"] = txtPassword.text.toString()


                                                    prefs.setUsuario(sharedUsuario)


                                                    db.GeneralDao().borrarTodo() //Limpiando
                                                    db.GeneralDao().insert(
                                                        General(
                                                        0,
                                                        "medicion",
                                                            todo.body.datos["medicion"].toString()
                                                        )
                                                    )

                                                    //Parametros
                                                    val generales = todo.body.generales

                                                    for(general in generales){
                                                        db.GeneralDao().insert(
                                                            General(
                                                                0,
                                                                general.parametro,
                                                                general.descripcion,
                                                                general.valor,
                                                                general.estado
                                                            )
                                                        )
                                                    }



                                                    runOnUiThread {
                                                        botonLogin.isEnabled= true
                                                        resumenActivity.putExtra("medicion", prefs.getUsuario()["medicion"])
                                                        startActivity(resumenActivity)
                                                    }

                                                }

                                            }

                                        }

                                        override fun onFailure(call: Call<TodoResponse>, t: Throwable) {
                                            runOnUiThread {

                                                tmensaje.text = "Hubo un error de conexión"
                                                botonLogin.isEnabled= true
                                            }

                                            //TODO(t.toString() + "fff")
                                        }

                                    })









                                }


                            }else if(usuario.error.equals("1")){
                                runOnUiThread {
                                    tmensaje.text = usuario.message
                                    botonLogin.isEnabled= true
                                }

                            }

                        }


                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        lifecycleScope.launch(Dispatchers.IO) {

                            var miUsu = db.UsuarioDao().get_email(txtEmail.text.toString())
                            if(miUsu!=null){
                                if(txtEmail.text.toString() == miUsu.email && txtPassword.text.toString() == miUsu.password){

                                    runOnUiThread {
                                        botonLogin.isEnabled= true
                                        val resumenActivity = Intent(baseContext, ResumenActivity::class.java)
                                        resumenActivity.putExtra("medicion", "")
                                        startActivity(resumenActivity)
                                    }

                                }else{
                                    runOnUiThread {

                                        tmensaje.text = "Usuario o contraseña incorrecta"
                                        botonLogin.isEnabled= true
                                    }
                                }
                            }else{
                                runOnUiThread {

                                    tmensaje.text = "Error en el servidor, intente luego"
                                    botonLogin.isEnabled= true
                                }
                            }
                            runOnUiThread {
                                botonLogin.isEnabled= true
                            }

                        }



                        // TODO(t.toString() + "fff")
                    }

                })
            }



        }
    }
}