package com.example.damer2

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.damer2.data.Database.AuditoriaDb
import com.example.damer2.data.Entities.Producto
import com.example.damer2.databinding.ActivityProductoAgregarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class ProductoAgregarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductoAgregarBinding
    val directorioTemp = Environment.DIRECTORY_PICTURES + "/temp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_producto_agregar)


        binding = ActivityProductoAgregarBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val db = AuditoriaDb(this)

        var cod_negocio = intent.getStringExtra("cod_negocio").toString()
        var direccion = intent.getStringExtra("direccion").toString()
        var cod_categoria = intent.getStringExtra("cod_categoria").toString()
        var ca_descripcion = intent.getStringExtra("ca_descripcion").toString()
        var cod_zona = intent.getStringExtra("cod_zona").toString()
        var cod_canal = intent.getStringExtra("cod_canal").toString()

        val producto_agregarProducto = findViewById<ImageView>(R.id.producto_agregarProducto)
        val producto_agregar_inputBuscarCodigo = findViewById<EditText>(R.id.producto_agregar_inputBuscarCodigo)
        val producto_agregarCerrar1 = findViewById<ImageView>(R.id.producto_agregarCerrar1)
        val producto_agregarCerrar2 = findViewById<ImageView>(R.id.producto_agregarCerrar2)
        val producto_agregarCerrar3 = findViewById<ImageView>(R.id.producto_agregarCerrar3)


        val btnAtras = findViewById<ImageView>(R.id.btnAtras)

        val productoActivity = Intent(baseContext, ProductoActivity::class.java)




        btnAtras.setOnClickListener{
            finish()
        }

        producto_agregarCerrar1.setOnClickListener {
            var directory = getExternalFilesDir(directorioTemp)!!
            var numFiles = directory.walk().count()
            var i = 0
            directory.walk().forEach {
                i++
                if(i==2){
                    it.delete()
                    listarDocumentos(getExternalFilesDir(directorioTemp)!!)
                }
            }
        }
        producto_agregarCerrar2.setOnClickListener {
            var directory = getExternalFilesDir(directorioTemp)!!
            var numFiles = directory.walk().count()
            var i = 0
            directory.walk().forEach {
                i++
                if(i==3){
                    it.delete()
                    listarDocumentos(getExternalFilesDir(directorioTemp)!!)
                }
            }
        }
        producto_agregarCerrar3.setOnClickListener {
            var directory = getExternalFilesDir(directorioTemp)!!
            var numFiles = directory.walk().count()
            var i = 0
            directory.walk().forEach {
                i++
                if(i==4){
                    it.delete()
                    listarDocumentos(getExternalFilesDir(directorioTemp)!!)
                }
            }
        }

        producto_agregarProducto.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val descripcion_producto = producto_agregar_inputBuscarCodigo.text.toString()
                val miNegocio = db.NegocioDao().get_codigo(cod_negocio)
                val numProductos = db.ProductoDao().getAllProductos_negocio(cod_negocio).size + 1

                var codigo_nuevo = cod_negocio + numProductos.toString()


                db.ProductoDao().insert(Producto(
                    0,
                    codigo_nuevo,
                    cod_categoria,
                    cod_negocio,
                    descripcion_producto,
                    "",
                    "",
                    "",
                    "",
                    "1",
                    ca_descripcion,
                    "0",
                    miNegocio.distrito,
                    cod_zona,
                    cod_canal,
                    "1"
                ))

                //Moviendo los archivos a la carpeta con nombre del sku creado
                var miProd = db.ProductoDao().get_by_codigo(codigo_nuevo)


                val imageSku1 = findViewById<ImageView>(R.id.imageSku1)
                val imageSku2 = findViewById<ImageView>(R.id.imageSku2)
                val imageSku3 = findViewById<ImageView>(R.id.imageSku3)

                var i = 0

                var directory = getExternalFilesDir(directorioTemp)!!
                directory.walk().forEach {

                    if(i>0){

                        val filePath: String = it.getPath()
                        val bitmap = BitmapFactory.decodeFile(filePath)

                        val bos = ByteArrayOutputStream()
                        bitmap.compress(CompressFormat.JPEG, 10, bos)
                        val bitmapdata = bos.toByteArray()

                        val fos = FileOutputStream(it)
                        fos.write(bitmapdata)
                        fos.flush()
                        fos.close()


                        val isSuccess = it.renameTo(File(getExternalFilesDir(Environment.DIRECTORY_PICTURES+"/" + miNegocio.codigo_negocio + "/"+codigo_nuevo).toString()+"/"+i+".jpg"))

                    }
                    i++
                }


                finish()
                /*runOnUiThread {

                    productoActivity.putExtra("cod_negocio", cod_negocio)
                    productoActivity.putExtra("direccion", direccion)
                    productoActivity.putExtra("cod_categoria", cod_categoria)
                    productoActivity.putExtra("cod_zona", cod_zona)
                    productoActivity.putExtra("cod_canal", cod_canal)
                    productoActivity.putExtra("ca_descripcion", ca_descripcion)
                    startActivity(productoActivity)

                }*/
            }


        }

        binding.openCamera.setOnClickListener {
            var directory = getExternalFilesDir(directorioTemp)!!
            var numFiles = directory.walk().count()

            if(numFiles <=3){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                    it.resolveActivity(packageManager).also { component ->
                        createPhotoFile()
                        val photoUri: Uri =
                            FileProvider.getUriForFile(
                                this,
                                BuildConfig.APPLICATION_ID + ".fileprovider", file
                            )
                        it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    }
                }
                openCamera.launch(intent)
            }else{
                Toast.makeText(applicationContext,"Máximo 3 fotos", Toast.LENGTH_SHORT).show()
            }


            //openCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }



    }

    private val openCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // val data = result.data!!
                // val bitmap = data.extras!!.get("data") as Bitmap
                val bitmap = getBitmap()


                binding.img.setImageBitmap(bitmap)
            }else if(result.resultCode == RESULT_CANCELED){
                var directory = getExternalFilesDir(directorioTemp)!!
                var numFiles = directory.walk().count()
                var i = 0
                directory.walk().forEach {
                    i++
                    if(i==numFiles){
                        it.delete()
                    }
                }
            }
            listarDocumentos(getExternalFilesDir(directorioTemp)!!)
        }
    private lateinit var file: File
    private fun createPhotoFile() {
        val dir = getExternalFilesDir(directorioTemp)!!
        file = File.createTempFile("IMG_${System.currentTimeMillis()}_", ".jpg", dir)
    }

    private fun listarDocumentos(directory: File) {
        val imageSku1 = findViewById<ImageView>(R.id.imageSku1)
        val imageSku2 = findViewById<ImageView>(R.id.imageSku2)
        val imageSku3 = findViewById<ImageView>(R.id.imageSku3)
        val producto_agregarCerrar1 = findViewById<ImageView>(R.id.producto_agregarCerrar1)
        val producto_agregarCerrar2 = findViewById<ImageView>(R.id.producto_agregarCerrar2)
        val producto_agregarCerrar3 = findViewById<ImageView>(R.id.producto_agregarCerrar3)

        producto_agregarCerrar1.isVisible = false
        producto_agregarCerrar2.isVisible = false
        producto_agregarCerrar3.isVisible = false
        imageSku1.setImageDrawable(getDrawable(R.drawable.blanco))
        imageSku2.setImageDrawable(getDrawable(R.drawable.blanco))
        imageSku3.setImageDrawable(getDrawable(R.drawable.blanco))

        var i = 0


        directory.walk().forEach {

            if(i==1) {
                producto_agregarCerrar1.isVisible = true
                imageSku1.setImageURI(it.toUri())
            }

            if(i==2) {
                producto_agregarCerrar2.isVisible = true
                imageSku2.setImageURI(it.toUri())
            }

            if(i==3) {
                producto_agregarCerrar3.isVisible = true
                imageSku3.setImageURI(it.toUri())
            }

            i++
        }
    }

    private fun saveToGallery() {
        val content = createContent()
        val uri = save(content)
        clearContents(content, uri)
        //Toast.makeText(this,getString(13),Toast.LENGTH_LONG).show()
    }

    private fun createContent(): ContentValues {
        val fileName = file.name
        val fileType = "image/jpg"
        return ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.Files.FileColumns.MIME_TYPE, fileType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    private fun save(content: ContentValues): Uri {
        var outputStream: OutputStream?
        var uri: Uri?
        application.contentResolver.also { resolver ->
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
            outputStream = resolver.openOutputStream(uri!!)
        }
        outputStream.use { output ->
            getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, output)
        }
        return uri!!
    }

    private fun clearContents(content: ContentValues, uri: Uri) {
        content.clear()
        content.put(MediaStore.MediaColumns.IS_PENDING,0)
        contentResolver.update(uri,content,null,null)
    }

    private fun getBitmap(): Bitmap {

        return BitmapFactory.decodeFile(file.toString())
    }

    private fun compressBitmap(bitmap: Bitmap, quality:Int):Bitmap{
        // Initialize a new ByteArrayStream
        val stream = ByteArrayOutputStream()

        // Compress the bitmap with JPEG format and specified quality
        bitmap.compress(
            Bitmap.CompressFormat.JPEG, quality, stream
        )

        val byteArray = stream.toByteArray()

        // Finally, return the compressed bitmap
        return BitmapFactory.decodeByteArray(
            byteArray, 0, byteArray.size
        )
    }
}