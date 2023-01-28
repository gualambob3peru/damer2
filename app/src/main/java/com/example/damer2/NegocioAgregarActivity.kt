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

class NegocioAgregarActivity : AppCompatActivity()  , AdapterView.OnItemSelectedListener{

    var spinnerDistrito: Spinner? = null
    var spinnerZona: Spinner? = null
    var spinnerCanal: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negocio_agregar)
        val db = AuditoriaDb(this)

        val negocio_agregar_btnGuardar = findViewById<ImageView>(R.id.negocio_agregar_btnGuardar)
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtDireccion = findViewById<EditText>(R.id.txtDireccion)
        val txtVendedor = findViewById<EditText>(R.id.txtVendedor)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        val btnAtras = findViewById<ImageView>(R.id.btnAtras)

        val intento1 = Intent(this, NegocioActivity::class.java)

        spinnerDistrito = findViewById<Spinner>(R.id.cboDistrito)
        spinnerDistrito!!.onItemSelectedListener = this

        spinnerZona = findViewById<Spinner>(R.id.cboZona)
        spinnerZona!!.onItemSelectedListener = this

        spinnerCanal = findViewById<Spinner>(R.id.cboCanal)
        spinnerCanal!!.onItemSelectedListener = this

        var miCon = this

        lifecycleScope.launch(Dispatchers.IO){
            var distritos = db.DistritoDao().getAll()
            var miArr = arrayOfNulls<String>(distritos.size)
            var miPos = arrayOfNulls<Int>(distritos.size)

            if(distritos !=null){
                for(ind in miArr.indices){
                    miArr[ind] = distritos[ind].descripcion
                }
            }

            var aa = ArrayAdapter(miCon, android.R.layout.simple_spinner_item, miArr)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDistrito!!.adapter = aa

            var zonas = db.ZonaDao().getAll()
            miArr = arrayOfNulls<String>(zonas.size)
            miPos = arrayOfNulls<Int>(zonas.size)
            if(zonas !=null){
                for(ind in miArr.indices){
                    miArr[ind] = zonas[ind].descripcion
                }
            }

            runOnUiThread {
                aa = ArrayAdapter(miCon, android.R.layout.simple_spinner_item,miArr)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerZona!!.adapter = aa
            }


            spinnerZona!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    lifecycleScope.launch(Dispatchers.IO){
                        var zonas = db.ZonaDao().getAll()
                        var it = parent.getSelectedItem().toString();
                        var miZona = db.ZonaDao().get_descripcion(it)
                        var misDistritos = db.DistritoDao().get_cod_zona(miZona.codigo)

                        miArr = arrayOfNulls<String>(misDistritos.size)
                        miPos = arrayOfNulls<Int>(misDistritos.size)
                        if(misDistritos !=null){
                            for(ind in miArr.indices){
                                miArr[ind] = misDistritos[ind].descripcion
                            }
                        }

                        runOnUiThread {
                            aa = ArrayAdapter(miCon, android.R.layout.simple_spinner_item,miArr)
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerDistrito!!.adapter = aa
                        }


                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }


            var canals = db.CanalDao().getAll()
            miArr = arrayOfNulls<String>(canals.size)
            miPos = arrayOfNulls<Int>(canals.size)

            if(canals !=null){
                for(ind in miArr.indices){
                    miArr[ind] = canals[ind].descripcion
                }
            }

            runOnUiThread {
                aa = ArrayAdapter(miCon, android.R.layout.simple_spinner_item, miArr)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCanal!!.adapter = aa
            }

        }

        negocio_agregar_btnGuardar.setOnClickListener {
            //Guardar el nuevo negocio en la BD Interna



            lifecycleScope.launch(Dispatchers.IO){

                var zonas = db.ZonaDao().getAll()
                var cod_cad_zona = zonas[spinnerZona!!.selectedItemId.toInt()].codigo

                var canals = db.CanalDao().getAll()
                var cod_cad_canal = canals[spinnerCanal!!.selectedItemId.toInt()].codigo

                var distritos = db.DistritoDao().getAll()
                var distriValor = spinnerDistrito!!.getSelectedItem().toString()
                var miDistri = db.DistritoDao().get_descripcion(distriValor)
                var cod_cad_distrito = miDistri.codigo

                var miNegocio = Negocio(
                    0,
                    "T"+(100000..999999).random(),
                    txtDireccion.text.toString(),
                    txtVendedor.text.toString(),
                    txtTelefono.text.toString(),
                    cod_cad_zona,
                    cod_cad_distrito,
                    cod_cad_canal,
                    txtNombre.text.toString(),
                    "1"
                )
                db.NegocioDao().insert(miNegocio)


                intento1.putExtra("medicion", UsuarioApplication.prefs.getUsuario()["medicion"])
                startActivity(intento1)
            }

        }
        btnAtras.setOnClickListener {
            intento1.putExtra("medicion", UsuarioApplication.prefs.getUsuario()["medicion"])
            startActivity(intento1)
        }

    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}
