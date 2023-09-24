package com.example.damer2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.global.GlobalVar.Companion.RUTA_API
import com.example.damer2.shared.UsuarioApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LinkServidorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_servidor)

        val db = AuditoriaDb(this)
        val txtLinkServidor = findViewById<EditText>(R.id.txtLinkServidor)
        val btnLinkServidorGuardar = findViewById<Button>(R.id.btnLinkServidorGuardar)
        val btnAtras = findViewById<ImageView>(R.id.btnAtras)

        runOnUiThread {
            txtLinkServidor.setText(RUTA_API)
        }

        btnLinkServidorGuardar.setOnClickListener {
            lifecycleScope.launch (Dispatchers.IO){
                val ruta = db.GlobalDao().get_codigo("ruta_api")
                if(ruta == null){
                    db.GlobalDao().insert(com.example.damer2.data.Entities.Global(
                        0,
                        "ruta_api",
                        "api",
                        txtLinkServidor.text.toString()
                    ))
                }else{
                    db.GlobalDao().update_codigo("ruta_api",txtLinkServidor.text.toString())
                }

                RUTA_API = txtLinkServidor.text.toString()
                runOnUiThread {
                    Toast.makeText(applicationContext,"Guardado correctamente", Toast.LENGTH_SHORT).show()

                }

            }
        }

        btnAtras.setOnClickListener {
            finish()
        }






    }


}