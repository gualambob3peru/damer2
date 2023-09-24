package com.example.damer2

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Negocio
import com.example.damer2.shared.UsuarioApplication
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
        val negocio_editar_btnLeocalizacion = findViewById<ImageView>(R.id.negocio_editar_btnLeocalizacion)
        val negocioLocalizacionActivity = Intent(baseContext, NegocioLocalizacionActivity::class.java)

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


        negocio_editar_btnLeocalizacion.setOnClickListener {
            runOnUiThread {

                negocioLocalizacionActivity.putExtra("cod_negocio", cod_negocio)
                startActivity(negocioLocalizacionActivity)
            }

        }
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }




}