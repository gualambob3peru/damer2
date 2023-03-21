package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout.DispatchChangeEvent
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Producto
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriaAgregarActivity  : AppCompatActivity(), AdapterView.OnItemSelectedListener  {


    var spinner: Spinner? = null
    var textView_msg: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_categoria_agregar)
        val db = AuditoriaDb(this)

        var codigo_negocio = intent.getStringExtra("cod_negocio")
        var cod_distrito = intent.getStringExtra("cod_distrito")
        var di_descripcion = intent.getStringExtra("di_descripcion")
        var direccion = intent.getStringExtra("direccion")
        val btnGuardarCategoria = findViewById<ImageView>(R.id.categoria_agregar_btnGuardar)
        val categoriaActivity = Intent(this, CategoriaActivity::class.java)
        var btnAtras = findViewById<ImageView>(R.id.btnAtras)

        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        val textView2 = findViewById<TextView>(R.id.txtMedicion)
        textView2.text = "MEDICION : "+ medicion.toString()

        spinner = findViewById<Spinner>(R.id.cboCategoria)
        spinner!!.onItemSelectedListener = this
        var miCon = this

        lifecycleScope.launch(Dispatchers.IO){
            val categorias = db.CategoriaDao().getAll()
            val contratos = db.ContratoDao().getAll()
            var misCategorias = arrayListOf<Categoria>()
            val miNegocio = db.NegocioDao().get_codigo(codigo_negocio.toString())
            val miZona = miNegocio.zona
            val miCanal = miNegocio.canal

            for(categoria in categorias){
                for(contrato in contratos){
                    if(contrato.cod_zona==miZona && contrato.cod_canal == miCanal && contrato.cod_categoria==categoria.codigo){
                        misCategorias.add(categoria)
                    }
                }
            }

            val miArr = arrayOfNulls<String>(misCategorias.size)
            var miPos = arrayOfNulls<Int>(misCategorias.size)

            if(misCategorias !=null){
                for(ind in miArr.indices){
                    miArr[ind] = misCategorias[ind].descripcion
                }
            }

            val aa = ArrayAdapter(miCon, android.R.layout.simple_spinner_item, miArr)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner!!.adapter = aa
        }

        btnGuardarCategoria.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){

                var categorias = db.CategoriaDao().getAll()


                val text: String = spinner!!.selectedItem.toString()
                val miId = spinner!!.selectedItemId.toInt()

                var cod_cad = categorias[miId].codigo
                var desc_cad = categorias[miId].descripcion

                db.ProductoDao().insert(Producto(0,
                    "",
                    cod_cad,
                    codigo_negocio.toString(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "1",
                    desc_cad)
                )

                finish()

            }
        }

        btnAtras.setOnClickListener {
            finish()
            /*runOnUiThread {
                categoriaActivity.putExtra("cod_negocio", codigo_negocio)
                categoriaActivity.putExtra("direccion", direccion)
                categoriaActivity.putExtra("cod_distrito", cod_distrito)
                categoriaActivity.putExtra("di_descripcion", di_descripcion)
                startActivity(categoriaActivity)
            }*/
        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}