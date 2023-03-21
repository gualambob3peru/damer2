package com.example.damer2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriaNegocioEditarActivity : AppCompatActivity()  , AdapterView.OnItemSelectedListener{

    var spinnerCanal: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categoria_negocio_editar)
        val db = AuditoriaDb(this)
        val cod_negocio = intent.getStringExtra("cod_negocio").toString()
        val txtDireccion = findViewById<TextView>(R.id.txtDireccion)
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtVendedor = findViewById<TextView>(R.id.txtVendedor)
        val txtTelefono = findViewById<TextView>(R.id.txtTelefono)
        val btnAtras = findViewById<ImageView>(R.id.btnAtras)
        val negocio_editar_btnGuardar = findViewById<ImageView>(R.id.negocio_editar_btnGuardar)

        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        val textView2 = findViewById<TextView>(R.id.txtMedicion)
        textView2.text = "MEDICION : "+ medicion.toString()


        spinnerCanal = findViewById<Spinner>(R.id.cboCanal)
        spinnerCanal!!.onItemSelectedListener = this

        var miCon = this

        lifecycleScope.launch(Dispatchers.IO){
            val negocio = db.NegocioDao().get_codigo(cod_negocio)
            txtDireccion.text = negocio.descripcion
            txtNombre.text = negocio.nombre
            txtVendedor.text = negocio.vendedor
            txtTelefono.text = negocio.telefono



            var canals = db.CanalDao().getAll()
            var miArr = arrayOfNulls<String>(canals.size)
            var miPos = arrayOfNulls<Int>(canals.size)

            if(canals !=null){
                for(ind in miArr.indices){
                    miArr[ind] = canals[ind].descripcion
                }
            }

            runOnUiThread {
                var aa = ArrayAdapter(miCon, android.R.layout.simple_spinner_item, miArr)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCanal!!.adapter = aa

                for(ind in miArr.indices){
                    if(canals[ind].codigo == negocio.canal){
                        spinnerCanal!!.setSelection(ind)
                    }

                }




            }

        }

        negocio_editar_btnGuardar.setOnClickListener {
            //Guardar el nuevo negocio en la BD Interna



            lifecycleScope.launch(Dispatchers.IO){
                val negocio = db.NegocioDao().get_codigo(cod_negocio)
                var canals = db.CanalDao().getAll()
                var cod_cad_canal = canals[spinnerCanal!!.selectedItemId.toInt()].codigo

                db.NegocioDao().update_varios(
                    txtDireccion.text.toString(),
                    txtNombre.text.toString(),
                    txtVendedor.text.toString(),
                    txtTelefono.text.toString(),
                    canals[spinnerCanal!!.selectedItemId.toInt()].codigo,
                    negocio.codigo_negocio)


            }
            finish()

        }
        btnAtras.setOnClickListener {
            finish()
        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}