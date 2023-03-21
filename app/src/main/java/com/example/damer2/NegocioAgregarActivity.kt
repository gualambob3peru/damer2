package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.time.Duration.Companion.seconds

class NegocioAgregarActivity : AppCompatActivity()  , AdapterView.OnItemSelectedListener{

    var spinnerDistrito: Spinner? = null
    var spinnerZona: Spinner? = null
    var spinnerCanal: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negocio_agregar)
        val db = AuditoriaDb(this)

        var cod_distrito = intent.getStringExtra("cod_distrito").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()

        val negocio_agregar_btnGuardar = findViewById<ImageView>(R.id.negocio_agregar_btnGuardar)
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtDireccion = findViewById<EditText>(R.id.txtDireccion)
        val txtVendedor = findViewById<EditText>(R.id.txtVendedor)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        val btnAtras = findViewById<ImageView>(R.id.btnAtras)

        val intento1 = Intent(this, NegocioActivity::class.java)

        var medicion = UsuarioApplication.prefs.getUsuario()["medicion"].toString()
        val textView2 = findViewById<TextView>(R.id.txtMedicion)
        textView2.text = "MEDICION : "+ medicion.toString()


        spinnerCanal = findViewById<Spinner>(R.id.cboCanal)
        spinnerCanal!!.onItemSelectedListener = this

        var miCon = this

        lifecycleScope.launch(Dispatchers.IO){

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
            }

        }

        negocio_agregar_btnGuardar.setOnClickListener {
            //Guardar el nuevo negocio en la BD Interna



            lifecycleScope.launch(Dispatchers.IO){

                var canals = db.CanalDao().getAll()
                var cod_cad_canal = canals[spinnerCanal!!.selectedItemId.toInt()].codigo

                val se= (System.currentTimeMillis().toString().substring(5).toInt() + 1).toString()

                var miNegocio = Negocio(
                    0,
                    se,
                    txtDireccion.text.toString(),
                    txtVendedor.text.toString(),
                    txtTelefono.text.toString(),
                    cod_zona,
                    cod_distrito,
                    cod_cad_canal,
                    txtNombre.text.toString(),
                    "1",
                    "1",
                    "0",
                    "",
                    0,
                    0,
                    1

                )
                db.NegocioDao().insert(miNegocio)

                finish()
            }

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
