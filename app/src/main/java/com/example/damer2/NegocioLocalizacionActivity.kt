package com.example.damer2

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.services.medicion.ProductoSubirInput
import com.example.damer2.services.negocio.NegocioLocalizacionInput
import com.example.damer2.services.negocio.NegocioResponse
import com.example.damer2.services.negocio.NegocioService
import com.example.damer2.services.todo.MedicionResponse
import com.example.damer2.services.todo.TodoService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NegocioLocalizacionActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    //private lateinit var googleMap: GoogleMap
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var markers: MutableList<Marker> = mutableListOf()
    lateinit var lat:String
    lateinit var lgn:String

    private companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negocio_localizacion)


        var cod_negocio = intent.getStringExtra("cod_negocio").toString()

        val btnAtras = findViewById<ImageView>(R.id.btnAtras)

        val btn_borrar_ubi = findViewById<Button>(R.id.btn_borrar_ubi)
        val btnEnviarUbi = findViewById<Button>(R.id.btnEnviarUbi)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        btnAtras.setOnClickListener {
            finish()

        }

        btnEnviarUbi.setOnClickListener {
            val negocioLocalizacionInput = NegocioLocalizacionInput(cod_negocio,lat,lgn)
            var serviceTodo = NegocioService.create()
            var apiInterfaceTodo = serviceTodo.subirLocalizacionNegocio(negocioLocalizacionInput)
            apiInterfaceTodo.enqueue( object : Callback<NegocioResponse> {
                override fun onResponse(
                    call: Call<NegocioResponse>,
                    response: Response<NegocioResponse>
                ){
                    runOnUiThread {
                        Toast.makeText(applicationContext,"UBICACIÓN ENVIADA", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<NegocioResponse>, t: Throwable) {
                    var a=  2
                }
            });
        }
        btn_borrar_ubi.setOnClickListener {
            deleteMarkers()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val db = AuditoriaDb(this)

        lat = intent.getStringExtra("lat").toString()
        lgn = intent.getStringExtra("lgn").toString()
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        enableMyLocation()
        // Habilita la ubicación del usuario y muestra el botón "Mi ubicación"
        // Añade un marcador en una ubicación específica
        if(lat==""){
            lat = "-12.0195177"
        }
        if(lgn == ""){
            lgn = "-77.0637"
        }

        val location = LatLng(lat.toDouble(), lgn.toDouble())
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
    // San
        // Francisco
      /*  mMap.addMarker(MarkerOptions().position(location).title("Lima"))

        // Mueve la cámara a la ubicación del marcador
       */
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))

        /*val request = FindAutocompletePredictionsRequest.builder()
            .setQuery("Peru lima")
            .build()*/
        /*val placesClient = Places.createClient(this)
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                if (response.autocompletePredictions.isNotEmpty()) {
                    val placeId = response.autocompletePredictions[0].placeId
                    val placeFields = listOf(Place.Field.LAT_LNG)

                    val fetchRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
                    placesClient.fetchPlace(fetchRequest)
                        .addOnSuccessListener { fetchResponse ->
                            val place = fetchResponse.place
                            val latLng = place.latLng

                            if (latLng != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            }
                        }
                }
            }*/


    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }
        mMap.isMyLocationEnabled = true
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

                    val builder = AlertDialog.Builder(this@NegocioLocalizacionActivity)
                    builder.setMessage("Datos Guardados")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar"){dialog, id ->
                            finish()
                        }
                    val alert = builder.create()
                    alert.show()
                }
            }
        }
    }

    private fun saveLocationToPrefs(latitude: Double, longitude: Double) {
        val cod_negocio = intent.getStringExtra("cod_negocio").toString()
        lifecycleScope.launch(Dispatchers.IO){
            val db = AuditoriaDb(applicationContext)
            if(latitude == 0.00){
                db.NegocioDao().update_location(cod_negocio,"","")
            }else{
                db.NegocioDao().update_location(cod_negocio,latitude.toString(),longitude.toString())

            }

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

    override fun onMapClick(latLng: LatLng) {

        if(markers.size==0){
            val marker = mMap.addMarker(MarkerOptions().position(latLng).title("Nuevo marcador"))
            if (marker != null) {
                markers.add(marker)
            }
            lat = latLng.latitude.toString()
            lgn = latLng.longitude.toString()
            saveLocationToPrefs(latLng.latitude,latLng.longitude)
            Toast.makeText(this, "Marcador agregado", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Ya existe un marcador en el mapa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteMarkers() {
        for (marker in markers) {
            marker.remove()
        }
        saveLocationToPrefs(0.00,0.00)
        Toast.makeText(this, "Marcador borrado", Toast.LENGTH_SHORT).show()

        markers.clear()
    }
}