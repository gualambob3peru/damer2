package com.example.damer2

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NegocioLocalizacionActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negocio_localizacion)

        var cod_negocio = intent.getStringExtra("cod_negocio").toString()

        val btnAtras = findViewById<ImageView>(R.id.btnAtras)
        val btn_guardar_ubi = findViewById<Button>(R.id.btn_guardar_ubi)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        lifecycleScope.launch(Dispatchers.IO) {

        }

        btnAtras.setOnClickListener {
            finish()

        }

        btn_guardar_ubi.setOnClickListener {
            getLastKnownLocation()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        enableMyLocation()
    }
    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }
        googleMap.isMyLocationEnabled = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    // Save the user's location to SharedPreferences
                    saveLocationToPrefs(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun saveLocationToPrefs(latitude: Double, longitude: Double) {
        val cod_negocio = intent.getStringExtra("cod_negocio").toString()
        lifecycleScope.launch(Dispatchers.IO){
            val db = AuditoriaDb(applicationContext)
            db.NegocioDao().update_location(cod_negocio,latitude.toString(),longitude.toString())

            runOnUiThread {
                Toast.makeText(applicationContext,"Guardado correctamente", Toast.LENGTH_SHORT).show()
            }
        }
        /*val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putFloat(KEY_LATITUDE, latitude.toFloat())
        editor.putFloat(KEY_LONGITUDE, longitude.toFloat())
        editor.apply()*/
    }
}